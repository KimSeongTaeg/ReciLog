package com.project.recilog.controller; // 컨트롤러 패키지 위치

import com.project.recilog.domain.Budget; // 예산 엔티티
import com.project.recilog.dto.BudgetRequestDto; // 예산 DTO
import com.project.recilog.service.BudgetService; // 서비스 인터페이스
import jakarta.validation.Valid; // 요청 유효성 검증
import lombok.RequiredArgsConstructor; // 생성자 주입
import org.springframework.http.ResponseEntity; // 응답 객체
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 인증 회원 추출
import org.springframework.web.bind.annotation.*; // REST 컨트롤러 어노테이션

import java.util.List; // 리스트

@RestController // RESTful 컨트롤러 선언
@RequestMapping("/api/v1/budgets") // 매핑 URL 공통 경로
@RequiredArgsConstructor // final 필드 생성자 주입
public class BudgetController { // 월별 목표 예산 제어 REST API 컨트롤러

    private final BudgetService budgetService; // 예산 서비스 주입

    @PostMapping // HTTP POST - 예산 설정/수정 API
    public ResponseEntity<String> setBudget(@AuthenticationPrincipal Long userId, // 인증된 회원 ID
                                            @Valid @RequestBody BudgetRequestDto dto) { // 입력 검증 DTO
        budgetService.saveOrUpdateBudget(userId, dto); // 서비스 실행
        return ResponseEntity.ok("목표 예산 설정이 완료되었습니다."); // 200 OK 응답 반환
    }

    @GetMapping // HTTP GET - 월별 예산 목록 조회 API
    public ResponseEntity<List<Budget>> getMonthlyBudgets(@AuthenticationPrincipal Long userId, // 인증된 회원 ID
                                                          @RequestParam String targetYearMonth) { // 조회 연월 쿼리 파라미터
        return ResponseEntity.ok(budgetService.getMonthlyBudgets(userId, targetYearMonth)); // 200 OK 응답 반환
    }
}