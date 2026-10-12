package com.project.recilog.dto.auth; // 인증 관련 DTO 패키지 위치

import jakarta.validation.constraints.Email; // 이메일 형식 검증 어노테이션 임포트
import jakarta.validation.constraints.NotBlank; // 필수값 및 공백 문자 검증 어노테이션 임포트
import lombok.Getter; // Getter 메서드 자동 생성 롬복 임포트
import lombok.NoArgsConstructor; // 기본 생성자 자동 생성 롬복 임포트

/**
 * [회원가입 요청 DTO]
 * - 클라이언트(프론트엔드)가 회원가입 시 전달하는 JSON 데이터를 받는 객체
 */
@Getter // 모든 필드의 Getter 메서드를 자동으로 생성함
@NoArgsConstructor // JSON 파싱(Jackson 라이브러리)을 위한 파라미터 없는 기본 생성자 자동 생성
public class SignUpRequestDto { // 회원가입 요청 클래스 정의

    @NotBlank(message = "이메일은 필수 입력 항목입니다.") // 빈 문자열(""), 공백(" "), null 값을 불허함
    @Email(message = "올바른 이메일 형식이 아닙니다.") // 이메일 규격(aaa@bbb.ccc) 형식인지 유효성 검증
    private String email; // 회원가입용 사용자 이메일 계정

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.") // 비밀번호 누락 방지 검증
    private String password; // 회원가입용 사용자 비밀번호 (서비스 레이어에서 BCrypt 암호화 처리됨)

    @NotBlank(message = "이름은 필수 입력 항목입니다.") // 사용자 이름 누락 방지 검증
    private String username; // 사용자 이름(닉네임)
}