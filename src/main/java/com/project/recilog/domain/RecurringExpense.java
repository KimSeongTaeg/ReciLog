package com.project.recilog.domain; // 도메인 엔티티 패키지 위치

import jakarta.persistence.*; // JPA 어노테이션 임포트
import lombok.*; // Lombok 어노테이션 임포트
import java.math.BigDecimal; // 금액 단위 객체 임포트

@Entity // JPA 엔티티 선언
@Table(name = "recurring_expenses") // DB "recurring_expenses" 테이블 매핑
@Getter // Getter 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
@AllArgsConstructor // 전체 인자 생성자
@Builder // 빌더 패턴
public class RecurringExpense { // 매달 정기적으로 나가는 고정 지출(월세, 구독 등) 엔티티

    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "recurring_id") // DB 컬럼명
    private Long recurringId; // 고정 지출 ID PK

    @ManyToOne(fetch = FetchType.LAZY) // 고정지출(N) : 사용자(1) 지연 로딩 매핑
    @JoinColumn(name = "user_id", nullable = false) // FK 매핑
    private User user; // 사용자 참조

    @ManyToOne(fetch = FetchType.LAZY) // 고정지출(N) : 카테고리(1) 지연 로딩 매핑
    @JoinColumn(name = "category_id", nullable = false) // FK 매핑
    private Category category; // 카테고리 참조

    @Column(name = "store_name", nullable = false, length = 100) // 결제처 상호명 (예: "넷플릭스")
    private String storeName; // 고정 지출처 이름

    @Column(nullable = false, precision = 12, scale = 0) // 결제 고정 금액
    private BigDecimal amount; // 정기 결제 금액

    @Column(name = "payment_day", nullable = false) // 매달 결제되는 일자 (1~31)
    private Integer paymentDay; // 정기 결제일

    @Column(name = "payment_method", nullable = false, length = 30) // 결제 수단
    private String paymentMethod; // 수단 (CARD, BANK 등)

    @Column(length = 200) // 추가 메모
    private String memo; // 메모 정보

    @Builder.Default // 빌더 기본값 설정
    @Column(name = "is_active", nullable = false) // 현재 활성화/구독 여부
    private Boolean isActive = true; // 기본값 활성화(true)

    public void updateActive(Boolean isActive) { // 고정 지출 활성화 상태 변경 메서드
        this.isActive = isActive; // 활성화 여부 업데이트
    }
}