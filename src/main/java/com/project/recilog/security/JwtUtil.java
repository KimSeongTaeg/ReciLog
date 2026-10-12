package com.project.recilog.security; // 보안 패키지 위치

import io.jsonwebtoken.Claims; // JWT 데이터 클레임 객체 임포트
import io.jsonwebtoken.Jwts; // JWT 빌더 클래스 임포트
import io.jsonwebtoken.SignatureAlgorithm; // 암호화 알고리즘 임포트
import io.jsonwebtoken.security.Keys; // 시크릿 키 생성 유틸 임포트
import org.springframework.stereotype.Component; // 빈 등록 어노테이션 임포트

import java.security.Key; // 자바 보안 Key 객체 임포트
import java.util.Date; // Date 객체 임포트

@Component // Spring 빈 컴포넌트 등록
public class JwtUtil { // JWT 토큰 생성, 검증, 파싱을 담당하는 전담 유틸리티 클래스

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HS256 알고리즘에 기반한 보안 키 자동 생성
    private final long accessTokenValidity = 1000L * 60 * 30; // Access Token 유효기간: 30분 (밀리초 단위)
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // Refresh Token 유효기간: 7일 (밀리초 단위)

    public String createAccessToken(Long userId, String email, String role) { // Access Token 생성 메서드
        Claims claims = Jwts.claims().setSubject(email); // 토큰 서브젝트로 회원 이메일 설정
        claims.put("userId", userId); // 클레임에 회원 ID 저장
        claims.put("role", role); // 클레임에 회원 권한 저장

        Date now = new Date(); // 현재 시간
        return Jwts.builder() // 빌더로 토큰 생성 시작
                .setClaims(claims) // 커스텀 클레임 주입
                .setIssuedAt(now) // 토큰 발급 시간 설정
                .setExpiration(new Date(now.getTime() + accessTokenValidity)) // 토큰 만료 시간 설정
                .signWith(key) // 보안 키로 디지털 서명
                .compact(); // 문자열 직렬화 후 반환
    }

    public String createRefreshToken(Long userId) { // Refresh Token 생성 메서드
        Claims claims = Jwts.claims(); // 클레임 객체 생성
        claims.put("userId", userId); // 회원 ID 담기

        Date now = new Date(); // 현재 시간
        return Jwts.builder() // 토큰 생성 시작
                .setClaims(claims) // 클레임 설정
                .setIssuedAt(now) // 발급 일시
                .setExpiration(new Date(now.getTime() + refreshTokenValidity)) // 만료 일시 설정
                .signWith(key) // 서명
                .compact(); // 직렬화
    }

    public boolean validateToken(String token) { // 토큰 유효성 및 변조/만료 검증 메서드
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); // 토큰 파싱 시도
            return true; // 에러가 없으면 유효한 토큰 (true)
        } catch (Exception e) { // 토큰 만료, 서명 불일치, 구조 파손 등의 예외 발생 시
            return false; // 위조되거나 만료된 토큰 (false)
        }
    }

    public Long getUserId(String token) { // 토큰 내부의 회원 ID 추출 메서드
        return Jwts.parserBuilder().setSigningKey(key).build() // 파서 생성
                .parseClaimsJws(token).getBody().get("userId", Long.class); // userId 클레임 값 파싱
    }

    public String getEmail(String token) { // 토큰 내부의 이메일 추출 메서드
        return Jwts.parserBuilder().setSigningKey(key).build() // 파서 생성
                .parseClaimsJws(token).getBody().getSubject(); // Subject(email) 반환
    }

    public long getAccessTokenValidity() { // 엑세스 토큰 만료시간 반환 메서드
        return accessTokenValidity; // 밀리초 반환
    }
}