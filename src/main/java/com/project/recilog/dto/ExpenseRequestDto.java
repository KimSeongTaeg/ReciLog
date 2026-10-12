package com.project.recilog.dto; // DTO 패키지 위치 선언

import jakarta.validation.constraints.NotBlank; // 빈 문자열("") 및 공백(" ") 검증 어노테이션 임포트
import jakarta.validation.constraints.NotNull; // null 값 검증 어노테이션 임포트
import lombok.Getter; // Getter 메서드 자동 생성 롬복 어노테이션 임포트
import lombok.NoArgsConstructor; // 기본 생성자 자동 생성 롬복 어노테이션 임포트
import java.math.BigDecimal; // 정밀한 금액 계산을 위한 BigDecimal 객체 임포트
import java.time.LocalDateTime; // 결제일시 저장을 위한 Java8 날짜 객체 임포트
import java.util.List; // 리스트 컬렉션 객체 임포트

/**
 * [지출 등록 및 수정 요청 DTO]
 * - 프론트엔드에서 수동 입력 또는 OCR 스캔 완료 후 서버로 지출 데이터(1:N 헤더+품목)를 전송할 때 사용하는 객체
 */
@Getter // Jackson JSON 직렬화 및 서비스 레이어 데이터 접근을 위한 Getter 자동 생성
@NoArgsConstructor // JSON 바인딩(Jackson)을 수행할 때 필수적으로 필요한 기본 생성자 자동 생성
public class ExpenseRequestDto { // 지출 요청 DTO 클래스 선언

    @NotNull(message = "카테고리는 필수 선택 항목입니다.") // null 값 입력 방지 유효성 검증
    private Integer categoryId; // 선택된 카테고리의 PK 식별자 ID

    @NotBlank(message = "상호명은 필수 입력 항목입니다.") // null, 빈 문자열(""), 공백(" ") 입력 방지 검증
    private String storeName; // 결제한 가맹점 가게 상호명

    @NotNull(message = "총 결제 금액은 필수 입력 항목입니다.") // null 값 방지 검증
    private BigDecimal totalAmount; // 결제된 총 금액 (부동 소수점 오차를 방지하기 위해 BigDecimal 사용)

    @NotNull(message = "결제 일시는 필수 입력 항목입니다.") // null 값 방지 검증
    private LocalDateTime spentAt; // 영수증 표기 실제 결제 발생 일시

    private String paymentMethod; // 결제 수단 (예: "CARD", "CASH" - 선택 입력 항목)
    private String memo; // 지출 관련 추가 메모 사항 (선택 입력 항목)

    private List<ExpenseItemDto> items; // 영수증 내부 세부 품목(1:N 하위 데이터) 리스트

    /**
     * [영수증 세부 품목 요청 중첩 DTO]
     * - 지출 헤더에 종속된 개별 세부 상품 항목 데이터를 전달받는 정적 내부 클래스
     */
    @Getter // 세부 품목 DTO용 Getter 자동 생성
    @NoArgsConstructor // 기본 생성자 자동 생성
    public static class ExpenseItemDto { // 정적 내부 클래스 선언
        @NotBlank(message = "품목명은 필수 입력 항목입니다.") // 필수 입력 검증
        private String itemName; // 세부 구매 상품명

        @NotNull(message = "수량은 필수 입력 항목입니다.") // 필수 입력 검증
        private Integer quantity; // 상품 구매 수량

        @NotNull(message = "단가는 필수 입력 항목입니다.") // 필수 입력 검증
        private BigDecimal unitPrice; // 상품 개당 단가

        @NotNull(message = "소계 금액은 필수 입력 항목입니다.") // 필수 입력 검증
        private BigDecimal subtotalPrice; // 수량 x 단가의 세부 소계 금액
    }
}