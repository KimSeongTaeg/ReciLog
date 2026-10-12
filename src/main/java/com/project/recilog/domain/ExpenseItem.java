package com.project.recilog.domain; // 도메인 엔티티 패키지 위치

import jakarta.persistence.*; // JPA 어노테이션 임포트
import lombok.*; // Lombok 어노테이션 임포트
import java.math.BigDecimal; // 정밀한 금액 계산을 위한 BigDecimal 임포트

@Entity // JPA 테이블 매핑 엔티티 클래스 선언
@Table(name = "expense_items") // DB의 "expense_items" 세부 품목 테이블과 매핑
@Getter // 필드 Getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 접근제한자 PROTECTED 설정
@AllArgsConstructor // 전체 인자 생성자
@Builder // 빌더 디자인 패턴 제공
public class ExpenseItem { // 영수증 내부 세부 품목(1:N의 N측) 엔티티

    @Id // 기본키 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment 채번
    @Column(name = "item_id") // DB 컬럼명
    private Long itemId; // 품목 고유 PK

    @ManyToOne(fetch = FetchType.LAZY) // 세부품목(N) : 지출헤더(1) 지연 로딩 관계 매핑
    @JoinColumn(name = "expense_id", nullable = false) // FK 컬럼 매핑
    private Expense expense; // 부모 지출 헤더 엔티티 참조 객체

    @Column(name = "item_name", nullable = false, length = 150) // 품목명 컬럼
    private String itemName; // 구매한 세부 상품 이름

    @Column(nullable = false) // 수량 컬럼
    private Integer quantity; // 구매 수량

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 0) // 단가 컬럼
    private BigDecimal unitPrice; // 상품 개당 단가

    @Column(name = "subtotal_price", nullable = false, precision = 12, scale = 0) // 소계 금액 컬럼
    private BigDecimal subtotalPrice; // 수량 x 단가의 세부 소계 금액

    // 연관관계 외래키 설정 보조 메서드
    public void assignExpense(Expense expense) { // 부모 지출 객체 할당 메서드
        this.expense = expense; // 부모 엔티티 외래키 필드 초기화
    }
}