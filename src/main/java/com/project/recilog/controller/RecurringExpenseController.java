package com.project.recilog.controller; // 컨트롤러 패키지 위치

import com.project.recilog.domain.RecurringExpense; // 고정지출 엔티티
import com.project.recilog.dto.RecurringExpenseRequestDto; // DTO
import com.project.recilog.service.RecurringExpenseService; // 서비스 인터페이스
import jakarta.validation.Valid; // 검증
import lombok.RequiredArgsConstructor; // 생성자 주입
import org.springframework.http.ResponseEntity; // 응답 객체
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 인증 추출
import org.springframework.web.bind.annotation.*; // REST 어노테이션

import java.util.List; // 리스트

@RestController // REST 컨트롤러
@RequestMapping("/api/v1/recurring-expenses") // API 공통 경로
@RequiredArgsConstructor // 생성자 주입
public class RecurringExpenseController { // 고정 지출 관리 REST API 컨트롤러

    private final RecurringExpenseService recurringExpenseService; // 서비스 주입

    @PostMapping // HTTP POST - 고정 지출 등록 API
    public ResponseEntity<String> createRecurringExpense(@AuthenticationPrincipal Long userId, // 인증 회원 ID
                                                         @Valid @RequestBody RecurringExpenseRequestDto dto) { // DTO
        recurringExpenseService.saveRecurringExpense(userId, dto); // 서비스 호출
        return ResponseEntity.ok("고정 지출이 성공적으로 등록되었습니다."); // 200 OK
    }

    @GetMapping // HTTP GET - 활성화된 고정 지출 목록 조회 API
    public ResponseEntity<List<RecurringExpense>> getActiveRecurringExpenses(@AuthenticationPrincipal Long userId) { // 회원 ID
        return ResponseEntity.ok(recurringExpenseService.getActiveRecurringExpenses(userId)); // 200 OK
    }
}