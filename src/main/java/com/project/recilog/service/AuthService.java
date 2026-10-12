package com.project.recilog.service; // 서비스 인터페이스 패키지 위치 선언

import com.project.recilog.dto.auth.*; // 인증 관련 DTO 클래스 전체 임포트

/**
 * [회원 인증 및 토큰 관리 비즈니스 로직 인터페이스]
 * - 회원가입, 로그인, 토큰 재발급, 로그아웃의 서비스 규격을 정의함
 */
public interface AuthService { // 회원 인증 서비스 인터페이스 선언

    /**
     * 신규 회원가입을 처리하는 메서드
     *
     * @param dto 회원가입에 필요한 이메일, 비밀번호, 이름 정보가 담긴 DTO
     */
    void signUp(SignUpRequestDto dto); // 회원가입 비즈니스 메서드 명세

    /**
     * 로그인 검증 후 JWT Access Token 및 Refresh Token을 발급하는 메서드
     *
     * @param dto 로그인 시도 이메일 및 비밀번호가 담긴 DTO
     * @return 발급된 JWT 토큰 정보와 사용자 프로필 정보가 담긴 DTO
     */
    TokenResponseDto login(LoginRequestDto dto); // 로그인 비즈니스 메서드 명세

    /**
     * 만료된 Access Token을 Refresh Token 검증을 거쳐 재발급하는 메서드
     *
     * @param dto 클라이언트가 보관 중이던 Refresh Token이 담긴 DTO
     * @return 신규 발급된 Access Token 정보가 담긴 DTO
     */
    TokenResponseDto refreshAccessToken(RefreshTokenRequestDto dto); // 토큰 재발급 비즈니스 메서드 명세

    /**
     * 사용자의 로그아웃을 처리하여 DB에 저장된 Refresh Token을 폐기하는 메서드
     *
     * @param userId 로그아웃을 요청한 회원의 고유 식별자 PK
     */
    void logout(Long userId); // 로그아웃 비즈니스 메서드 명세
}