package com.project.recilog.security; // 보안 패키지 위치

import jakarta.servlet.FilterChain; // 서블릿 필터 체인 임포트
import jakarta.servlet.ServletException; // 서블릿 예외 임포트
import jakarta.servlet.http.HttpServletRequest; // HTTP 요청 객체 임포트
import jakarta.servlet.http.HttpServletResponse; // HTTP 응답 객체 임포트
import lombok.RequiredArgsConstructor; // 생성자 주입 어노테이션 임포트
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // 시큐리티 인증 토큰 임포트
import org.springframework.security.core.context.SecurityContextHolder; // 시큐리티 컨텍스트 홀더 임포트
import org.springframework.stereotype.Component; // 빈 등록 임포트
import org.springframework.web.filter.OncePerRequestFilter; // 단일 요청당 1회 실행 필터 임포트

import java.io.IOException; // 입출력 예외 임포트
import java.util.List; // 리스트 임포트

@Component // Spring 컴포넌트 빈 등록
@RequiredArgsConstructor // final 필드 생성자 자동 주입
public class JwtAuthenticationFilter extends OncePerRequestFilter { // HTTP 요청 하나당 딱 한 번씩 수행되는 JWT 검증 필터

    private final JwtUtil jwtUtil; // JWT 유틸리티 객체 주입

    @Override // OncePerRequestFilter 필터링 메서드 재정의
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization"); // HTTP 헤더에서 Authorization 추출

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // Authorization 헤더가 존재하고 Bearer 타입인 경우
            String token = authHeader.substring(7); // "Bearer " 접두어 7자리를 자르고 순수 토큰 문자열만 추출
            if (jwtUtil.validateToken(token)) { // 토큰 유효성 위조/만료 검증
                Long userId = jwtUtil.getUserId(token); // 토큰 내부 회원 ID 추출
                String email = jwtUtil.getEmail(token); // 토큰 내부 이메일 추출

                // SecurityContext에 등록할 인증 인메모리 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, email, List.of(() -> "ROLE_USER"));

                // SecurityContextHolder에 인증 성공 객체 저장 (이후 컨트롤러에서 @AuthenticationPrincipal로 접속 회원 정보 즉시 꺼냄)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response); // 다음 서블릿 필터로 요청 체인 전달
    }
}