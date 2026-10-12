package com.project.recilog.dto; // DTO 패키지 위치

import jakarta.validation.constraints.NotBlank; // 필수 검증
import jakarta.validation.constraints.NotNull; // 필수 검증
import lombok.Getter; // Getter 생성
import lombok.NoArgsConstructor; // 기본 생성자

import java.math.BigDecimal; // 금액 객체

@Getter // Getter 생성
@NoArgsConstructor // 기본 생성자 생성
public class RecurringExpenseRequestDto { // 고정 지출 등록 요청 DTO
    @NotNull // 필수값
    private Integer categoryId; // 카테고리 ID

    @NotBlank // 필수값
    private String storeName; // 지출처 상호명

    @NotNull // 필수값
    private BigDecimal amount; // 정기 지출 금액

    @NotNull // 필수값
    private Integer paymentDay; // 매달 결제되는 일자 (1~31)

    private String paymentMethod; // 결제 수단
    private String memo; // 메모
}