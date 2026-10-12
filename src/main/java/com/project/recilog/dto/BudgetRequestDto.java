package com.project.recilog.dto; // DTO 패키지 위치

import jakarta.validation.constraints.NotBlank; // 필수 문자열 검증
import jakarta.validation.constraints.NotNull; // 필수 객체 검증
import lombok.Getter; // Getter 생성
import lombok.NoArgsConstructor; // 기본 생성자
import java.math.BigDecimal; // 금액 객체

@Getter // Getter 자동 생성
@NoArgsConstructor // 기본 생성자 생성
public class BudgetRequestDto { // 예산 설정 및 수정 요청 DTO
    @NotNull // 필수값 지정
    private Integer categoryId; // 대상 카테고리 ID

    @NotBlank // 필수 빈문자열 불가
    private String targetYearMonth; // 목표 연월 (예: "2026-10")

    @NotNull // 필수값 지정
    private BigDecimal targetAmount; // 목표 예산 금액
}