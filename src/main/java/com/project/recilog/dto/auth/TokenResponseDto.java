package com.project.recilog.dto.auth; // 인증 관련 DTO 패키지 위치

import lombok.Builder; // 빌더 패턴 자동 생성 임포트
import lombok.Getter; // Getter 자동 생성 임포트

/**
 * [인증 토큰 응답 DTO]
 * - 로그인 성공 또는 토큰 재발급 성공 시 클라이언트에게 전달하는 JWT 및 사용자 정보 응답 객체
 */
@Getter // 모든 필드의 Getter 자동 생성
@Builder // 안전한 응답 객체 구성을 위한 빌더 디자인 패턴 제공
public class TokenResponseDto { // 토큰 응답 클래스 정의

    private String accessToken; // 백엔드 API 접근 권한 인증용 JWT Access Token (유효기간: 30분)
    private String refreshToken; // Access Token 만료 시 재발급을 요청하기 위한 JWT Refresh Token (유효기간: 7일)
    private String tokenType; // 토큰 인증 타입 (기본값: "Bearer")
    private Long expiresIn; // Access Token의 남은 유효 시간 (초 단위 단위 값)
    private String username; // 로그인에 성공한 회원 이름 (프론트엔드 UI 상단 닉네임 표시용)
    private String email; // 로그인에 성공한 회원 이메일
}