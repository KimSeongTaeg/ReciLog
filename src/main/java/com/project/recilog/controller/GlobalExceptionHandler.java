package com.project.recilog.controller; // 컨트롤러 패키지 위치

import org.springframework.http.ResponseEntity; // HTTP 응답 객체
import org.springframework.web.bind.annotation.ExceptionHandler; // 예외 핸들러 어노테이션
import org.springframework.web.bind.annotation.RestControllerAdvice; // 전역 컨트롤러 예외 처리 어노테이션

import java.util.Map; // Map 클래스

@RestControllerAdvice // 애플리케이션 전역에서 발생하는 예외를 일괄 감지하여 REST JSON 응답으로 전환
public class GlobalExceptionHandler { // 공통 예외 처리 컨트롤러 어드바이스

    @ExceptionHandler(IllegalArgumentException.class) // 비즈니스 로직 상의 잘못된 인자 및 검증 실패 예외 감지
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage())); // 400 Bad Request와 메시지 반환
    }

    @ExceptionHandler(Exception.class) // 감지되지 않은 기타 일반 서버 시스템 예외 감지
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        return ResponseEntity.internalServerError().body(Map.of("message", "서버 내부 오류가 발생했습니다.")); // 500 Server Error 반환
    }
}