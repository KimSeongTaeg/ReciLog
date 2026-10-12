package com.project.recilog.dto; // DTO 패키지 위치

import com.project.recilog.domain.Expense; // 지출 엔티티 임포트
import lombok.Builder; // 빌더 어노테이션 임포트
import lombok.Getter; // Getter 어노테이션 임포트
import java.math.BigDecimal; // 금액 객체 임포트
import java.time.LocalDateTime; // 날짜 시간 객체 임포트
import java.util.List; // 리스트 임포트

@Getter // DTO 필드 조회용 Getter 자동 생성
@Builder // 객체 생성을 위한 빌더 어노테이션
public class ExpenseResponseDto { // 클라이언트 응답용 지출 데이터 전달 객체 (DTO)

    private Long expenseId; // 지출 ID
    private Integer categoryId; // 카테고리 ID
    private String categoryName; // 카테고리 한글명 (뷰표시용)
    private String storeName; // 상호명
    private BigDecimal totalAmount; // 총 금액
    private LocalDateTime spentAt; // 결제일시
    private String paymentMethod; // 결제 수단
    private String memo; // 추가 메모
    private List<ExpenseItemResponseDto> items; // 세부 품목 리스트 응답 DTO

    @Getter // 세부 품목 DTO용 Getter
    @Builder // 세부 품목 DTO 빌더
    public static class ExpenseItemResponseDto { // 내부 정적 세부 품목 DTO 클래스
        private Long itemId; // 품목 ID
        private String itemName; // 품목 이름
        private Integer quantity; // 수량
        private BigDecimal unitPrice; // 단가
        private BigDecimal subtotalPrice; // 소계
    }

    // [정적 팩토리 메서드 pattern] - JPA Entity를 클라이언트 전송용 DTO로 직접 변환
    // (이유: 엔티티를 API로 직접 반환하면 DB구조 노출 및 JPA 양방향 연관관계 순환 참조(Infinite Recursion) 예외가 발생하므로 DTO로 무조건 변환)
    public static ExpenseResponseDto fromEntity(Expense entity) { // 엔티티 -> DTO 매핑 메서드
        return ExpenseResponseDto.builder() // 빌더 시작
                .expenseId(entity.getExpenseId()) // 지출 ID 매핑
                .categoryId(entity.getCategory().getCategoryId()) // 카테고리 ID 매핑
                .categoryName(entity.getCategory().getName()) // 카테고리 이름 매핑
                .storeName(entity.getStoreName()) // 상호명 매핑
                .totalAmount(entity.getTotalAmount()) // 총 금액 매핑
                .spentAt(entity.getSpentAt()) // 결제시간 매핑
                .paymentMethod(entity.getPaymentMethod()) // 결제 수단 매핑
                .memo(entity.getMemo()) // 메모 매핑
                .items(entity.getItems().stream() // 지출 내세부 품목 Stream 변환
                        .map(i -> ExpenseItemResponseDto.builder() // 자식 엔티티 -> 자식 DTO 변환
                                .itemId(i.getItemId()) // 품목 ID 매핑
                                .itemName(i.getItemName()) // 품목명 매핑
                                .quantity(i.getQuantity()) // 수량 매핑
                                .unitPrice(i.getUnitPrice()) // 단가 매핑
                                .subtotalPrice(i.getSubtotalPrice()) // 소계 매핑
                                .build()) // 자식 DTO 빌드
                        .toList()) // Stream 결과를 List로 수집 (Java 16+ toList 사용)
                .build(); // DTO 생성 완료 후 반환
    }
}