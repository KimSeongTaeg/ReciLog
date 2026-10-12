package com.project.recilog.service.impl; // 서비스 구현체 패키지 위치 선언

import com.project.recilog.domain.RefreshToken; // 리프레시 토큰 엔티티 임포트
import com.project.recilog.domain.User; // 회원 엔티티 임포트
import com.project.recilog.dto.auth.*; // 인증 관련 DTO 전체 임포트
import com.project.recilog.repository.RefreshTokenRepository; // 리프레시 토큰 DB 리포지토리 임포트
import com.project.recilog.repository.UserRepository; // 회원 DB 리포지토리 임포트
import com.project.recilog.security.JwtUtil; // JWT 생성 및 검증 유틸리티 임포트
import com.project.recilog.service.AuthService; // 인증 서비스 인터페이스 임포트
import lombok.RequiredArgsConstructor; // final 필드 생성자 자동 주입 롬복 임포트
import org.springframework.security.crypto.password.PasswordEncoder; // BCrypt 비밀번호 암호화 인터페이스 임포트
import org.springframework.stereotype.Service; // 서비스 컴포넌트 선언 어노테이션 임포트
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 처리 어노테이션 임포트

import java.time.LocalDateTime; // 날짜 시간 객체 임포트

@Service // Spring 컨테이너에 서비스 빈으로 등록
@RequiredArgsConstructor // final로 선언된 모든 필드에 대해 생성자를 자동 생성하여 의존성 주입(DI)을 수행
@Transactional(readOnly = true) // 클래스 내부 기본 트랜잭션을 읽기 전용으로 설정하여 DB 조회 성능을 최적화함
public class AuthServiceImpl implements AuthService { // 인증 서비스 구현체 클래스 선언

    private final UserRepository userRepository; // 사용자 DB 처리 인터페이스 주입
    private final RefreshTokenRepository refreshTokenRepository; // 리프레시 토큰 DB 처리 인터페이스 주입
    private final PasswordEncoder passwordEncoder; // BCrypt 비밀번호 암호화 객체 주입
    private final JwtUtil jwtUtil; // JWT 생성/검증 유틸리티 주입

    @Override // 인터페이스 메서드 재정의
    @Transactional // CUD(등록/수정/삭제) 발생 메서드이므로 쓰기 전용 트랜잭션 부여 (에러 발생 시 롤백 보장)
    public void signUp(SignUpRequestDto dto) { // 회원가입 비즈니스 로직 구현
        if (userRepository.existsByEmail(dto.getEmail())) { // 1. 이미 동일한 이메일로 가입된 회원이 있는지 DB 검사
            throw new IllegalArgumentException("이미 사용 중인 이메일 주소입니다."); // 중복 이메일 존재 시 예외 발생
        }

        User user = User.builder() // 2. 회원 엔티티 객체 생성 시작
                .email(dto.getEmail()) // 이메일 매핑
                .password(passwordEncoder.encode(dto.getPassword())) // 비밀번호를 BCrypt 알고리즘으로 단방향 해시 암호화하여 저장
                .username(dto.getUsername()) // 사용자 이름 매핑
                .provider("LOCAL") // 기본 제공자 LOCAL 설정
                .role("ROLE_USER") // 일반 사용자 권한 지정
                .status("ACTIVE") // 계정 활성화 상태 지정
                .build(); // 엔티티 객체 빌드 완료

        userRepository.save(user); // 3. DB에 회원 정보 영속화 (INSERT)
    }

    @Override // 로그인 메서드 재정의
    @Transactional // 토큰 DB 저장/갱신을 위해 쓰기 트랜잭션 적용
    public TokenResponseDto login(LoginRequestDto dto) { // 로그인 인증 로직 구현
        User user = userRepository.findByEmail(dto.getEmail()) // 1. 전달받은 이메일로 회원이 존재하는지 DB 조회
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.")); // 회원 미존재 시 예외 처리

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) { // 2. 입력된 평문 비밀번호와 DB 암호화 비밀번호 일치 여부 검증
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."); // 비밀번호 불일치 시 예외 처리
        }

        // 3. JWT 토큰 발급 (Access Token 30분, Refresh Token 7일)
        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getEmail(), user.getRole()); // Access Token 생성
        String refreshTokenStr = jwtUtil.createRefreshToken(user.getUserId()); // Refresh Token 생성

        // 4. 기존 DB에 저장된 사용자의 Refresh Token이 있는지 확인
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getUserId())
                .orElse(RefreshToken.builder().userId(user.getUserId()).build()); // 없으면 새로 엔티티 생성

        refreshToken.updateToken(refreshTokenStr, LocalDateTime.now().plusDays(7)); // 5. 신규 토큰 값과 만료 시간(7일 뒤) 세팅
        refreshTokenRepository.save(refreshToken); // 6. Refresh Token 정보 DB 업데이트/저장

        // 7. 클라이언트로 전달할 응답 DTO 구성 후 반환
        return TokenResponseDto.builder()
                .accessToken(accessToken) // Access Token 세팅
                .refreshToken(refreshTokenStr) // Refresh Token 세팅
                .tokenType("Bearer") // 인증 타입 지정
                .expiresIn(jwtUtil.getAccessTokenValidity() / 1000) // 엑세스 토큰 남은 시간(초 단위) 계산
                .username(user.getUsername()) // 사용자 이름 세팅
                .email(user.getEmail()) // 이메일 세팅
                .build(); // DTO 반환
    }

    @Override // 토큰 재발급 메서드 재정의
    @Transactional // 토큰 정보 갱신을 위한 쓰기 트랜잭션
    public TokenResponseDto refreshAccessToken(RefreshTokenRequestDto dto) { // 엑세스 토큰 재발급 비즈니스 로직
        String reqRefreshToken = dto.getRefreshToken(); // 클라이언트가 보낸 Refresh Token 추출
        if (!jwtUtil.validateToken(reqRefreshToken)) { // 1. Refresh Token 문자열 자체가 유효한지(서명, 만료 여부) 검증
            throw new IllegalArgumentException("유효하지 않거나 만료된 Refresh Token입니다."); // 검증 실패 시 예외 처리
        }

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(reqRefreshToken) // 2. DB에 실제 해당 토큰 세션이 존재하는지 확인
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Refresh Token입니다.")); // DB에 없으면 폐기된 토큰이므로 예외 처리

        User user = userRepository.findById(refreshToken.getUserId()) // 3. 토큰 소유 회원이 DB에 존재하는지 확인
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtUtil.createAccessToken(user.getUserId(), user.getEmail(), user.getRole()); // 4. 새로운 Access Token 발급

        return TokenResponseDto.builder() // 5. 신규 발급된 엑세스 토큰 정보 반환
                .accessToken(newAccessToken) // 새로 생성한 Access Token
                .refreshToken(reqRefreshToken) // 기존 Refresh Token 유지
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenValidity() / 1000)
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override // 로그아웃 메서드 재정의
    @Transactional // DB 데이터 삭제를 위한 쓰기 트랜잭션
    public void logout(Long userId) { // 로그아웃 로직 구현
        refreshTokenRepository.deleteByUserId(userId); // DB에서 해당 사용자의 Refresh Token을 삭제하여 향후 토큰 재발급 차단
    }
}