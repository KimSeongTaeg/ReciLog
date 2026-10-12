package com.project.recilog.dto.auth; // 인증 관련 DTO 패키지 위치

import jakarta.validation.constraints.NotBlank; // 필수값 검증 어노테이션 임포트
import lombok.Getter; // Getter 생성 임포트
import lombok.NoArgsConstructor; // 기본 생성자 생성 임포트

/**
 * [토큰 재발급 요청 DTO]
 * - Access Token 만료 시(401 Unauthorized 발생 시) Refresh Token을 실어 재발급 요청을 보낼 때 사용하는 객체
 */
@Getter // Getter 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
public class RefreshTokenRequestDto { // 토큰 재발급 요청 클래스 정의

    @NotBlank(message = "Refresh Token은 필수 항목입니다.") // 빈 토큰 값 전달 방지 유효성 검증
    private String refreshToken; // 클라이언트가 보관 중이던 JWT Refresh Token 문자열
}