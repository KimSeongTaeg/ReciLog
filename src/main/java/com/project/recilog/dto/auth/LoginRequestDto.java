package com.project.recilog.dto.auth; // 인증 관련 DTO 패키지 위치

import jakarta.validation.constraints.Email; // 이메일 형식 유효성 검사 임포트
import jakarta.validation.constraints.NotBlank; // 필수 문자열 유효성 검사 임포트
import lombok.Getter; // Getter 생성 임포트
import lombok.NoArgsConstructor; // 기본 생성자 생성 임포트

/**
 * [로그인 요청 DTO]
 * - 클라이언트가 이메일/비밀번호 기반 로그인 시 요청 Body로 전달하는 데이터 객체
 */
@Getter // Getter 자동 생성
@NoArgsConstructor // Jackson JSON 바인딩용 기본 생성자 생성
public class LoginRequestDto { // 로그인 요청 클래스 정의

    @NotBlank(message = "이메일을 입력해 주세요.") // 이메일 미입력 검증
    @Email(message = "올바른 이메일 형식이 아닙니다.") // 이메일 정규식 유효성 검증
    private String email; // 로그인 시도 이메일

    @NotBlank(message = "비밀번호를 입력해 주세요.") // 비밀번호 미입력 검증
    private String password; // 로그인 시도 원문(Plain Text) 비밀번호
}