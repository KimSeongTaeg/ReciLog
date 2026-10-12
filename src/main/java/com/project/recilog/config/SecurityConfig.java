package com.project.recilog.config; // 설정 클래스 패키지 위치

import com.project.recilog.security.JwtAuthenticationFilter; // 사용자 정의 JWT 필터 클래스 임포트
import lombok.RequiredArgsConstructor; // final 필드 자동 생성자 롬복 임포트
import org.springframework.context.annotation.Bean; // 스프링 빈 등록 어노테이션 임포트
import org.springframework.context.annotation.Configuration; // 설정 클래스 명시 어노테이션 임포트
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // HTTP 보안 설정 객체 임포트
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // 웹 보안 활성화 어노테이션 임포트
import org.springframework.security.config.http.SessionCreationPolicy; // 세션 정책 설정 클래스 임포트
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // BCrypt 단방향 비밀번호 암호화 객체 임포트
import org.springframework.security.crypto.password.PasswordEncoder; // 비밀번호 암호화 인터페이스 임포트
import org.springframework.security.web.SecurityFilterChain; // Security 필터 체인 인터페이스 임포트
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // 기본 인증 필터 클래스 임포트
import org.springframework.web.cors.CorsConfiguration; // CORS 세부 설정 객체 임포트
import org.springframework.web.cors.CorsConfigurationSource; // CORS 설정 출처 인터페이스 임포트
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // URL 기반 CORS 설정 구성체 임포트

import java.util.List; // 리스트 컬렉션 임포트

@Configuration // 이 클래스가 Spring의 빈 설정 관련 클래스임을 선언
@EnableWebSecurity // Spring Security 기능을 활성화하고 Web Security 지원을 추가함
@RequiredArgsConstructor // 의존성 주입(DI)을 위한 생성자 자동 생성
public class SecurityConfig { // 웹 보안 체인 및 CORS를 제어하는 설정 클래스

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 의존성 주입받을 JWT 토큰 필터 객체

    @Bean // Spring 컨테이너에 SecurityFilterChain 객체를 빈으로 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Security 필터 체인 빌더
        http
                .csrf(csrf -> csrf.disable()) // REST API 환경이므로 Cross-Site Request Forgery 방어 기능을 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // [CORS 허용 적용] 프론트엔드 출처 허용 세팅 적용
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 인증 방식을 사용하므로 서버 세션을 생성하지 않는 STATELESS 정책 적용
                .authorizeHttpRequests(auth -> auth // URL별 요청 권한 설정 시작
                        .requestMatchers("/api/v1/auth/**", "/h2-console/**").permitAll() // 로그인, 회원가입 API 및 H2 콘솔 URL은 인증 없이 누구나 접근 허용
                        .anyRequest().authenticated() // 그 외의 모든 API 엔드포인트 요청은 반드시 인증된 사용자만 접근 허용
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 화면이 iframe 내에서 구동되므로 frameOptions 비활성화
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter를 스프링 시큐리티 기본 인증 필터보다 앞서 실행되도록 체인에 추가

        return http.build(); // 설정이 완료된 SecurityFilterChain 객체 빌드 후 반환
    }

    @Bean // [CORS 세부 설정 정의 메서드] 프론트엔드(5173) 교차 출처 요청 완전 허용
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. 프론트엔드 출처 주소 허용 (로컬 개발용 5173 포트 및 추후 배포용 도메인 지정)
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));

        // 2. 허용할 HTTP 메서드 지정 (Preflight 검증용 OPTIONS 필수 포함)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 3. 허용할 HTTP 요청 헤더 지정 (Authorization 토큰 전송용)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));

        // 4. 자격 증명(쿠키/Bearer 토큰) 포함 요청 허용
        configuration.setAllowCredentials(true);

        // 5. Preflight 캐싱 시간 설정 (초 단위: 3600초 = 1시간 동안 재검증 생략)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 API 엔드포인트 경로에 CORS 설정 매핑
        return source;
    }

    @Bean // 비밀번호 암호화 빈 등록
    public PasswordEncoder passwordEncoder() { // 사용자 비밀번호 보안 암호화 빈
        return new BCryptPasswordEncoder(); // BCrypt 해시 함수 알고리즘을 사용한 암호화 객체 반환
    }
}