package com.project.recilog.domain; // 도메인엔티티 패키지 위치

import jakarta.persistence.*; // JPA 영속성 표준 라이브러리 어노테이션 전체 임포트
import lombok.*; // Lombok 라이브러리 어노테이션 전체 임포트
import java.math.BigDecimal; // 정밀한 금액 계산을 위한 BigDecimal 임포트
import java.time.LocalDateTime; // 결제 일시 저장용 Java8 날짜 객체 임포트
import java.util.ArrayList; // List 구현체 클래스 임포트
import java.util.List; // 연관관계 List 인터페이스 임포트

@Entity // JPA가 관리하는 데이터베이스 테이블 매핑 클래스임을 선언
@Table(name = "expenses") // DB의 "expenses" 테이블과 직접 매핑
@Getter // 엔티티 내 모든 필드의 Getter 메서드 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 생성 (외부 직접 객체 생성을 막기 위해 PROTECTED로 제한)
@AllArgsConstructor // 모든 필드를 인자로 받는 전체 생성자 생성
@Builder // 안전한 객체 생성을 위한 빌더 패턴 적용
public class Expense { // 지출 정보 헤더(Header) 테이블 매핑 엔티티

    @Id // 기본키(Primary Key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB의 Auto Increment 전략을 사용하여 ID 자동 증가
    @Column(name = "expense_id") // DB 컬럼명을 "expense_id"로 지정
    private Long expenseId; // 지출 고유식별자 ID

    @ManyToOne(fetch = FetchType.LAZY) // 지출(N) : 사용자(1) 다대일 지출 연관관계 (성능 최적화를 위해 지연 로딩 LAZY 설정)
    @JoinColumn(name = "user_id", nullable = false) // 외래키(FK) 컬럼 지정 및 필수값 constraint 설정
    private User user; // 지출을 등록한 회원 엔티티 객체

    @ManyToOne(fetch = FetchType.LAZY) // 지출(N) : 카테고리(1) 다대일 카테고리 연관관계 (지연 로딩 적용)
    @JoinColumn(name = "category_id", nullable = false) // 외래키 컬럼 지정
    private Category category; // 지출이속한 카테고리 엔티티 객체

    @Column(name = "store_name", nullable = false, length = 100) // 가맹점 상호명 컬럼 (NOT NULL, 최대 100자)
    private String storeName; // 결제한 가게 상호명

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 0) // 정밀도 12자리 금액 컬럼 (부동 소수점 오차 방지)
    private BigDecimal totalAmount; // 총 지출 결제 금액

    @Column(name = "spent_at", nullable = false) // 실제 결제 발생 일시 컬럼
    private LocalDateTime spentAt; // 영수증 표기 결제 일시

    @Column(name = "payment_method", nullable = false, length = 30) // 결제 수단 컬럼 (CARD, CASH 등)
    private String paymentMethod; // 사용된 결제 수단

    @Column(length = 500) // 메모 정보 컬럼 (최대 500자, NULL 허용)
    private String memo; // 지출 관련 추가 메모사항

    @Builder.Default // 빌더 패턴 기본값 유지 어노테이션
    @Column(name = "is_deleted", nullable = false) // 소프트 삭제(Soft Delete) 플래그 컬럼
    private Boolean isDeleted = false; // 물리적 삭제 대신 논리적 삭제 여부 판별값 (기본값 false)

    @Builder.Default // 빌더 패턴 기본값 지정
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true) // 1:N 양방향 연관관계 설정 (CascadeType.ALL로 헤더 저장 시 세부 항목 함께 영속화, orphanRemoval로 고아 객체 자동 삭제)
    private List<ExpenseItem> items = new ArrayList<>(); // 지출 세부 품목 리스트

    // [양방향 연관관계 편의 메서드] - 지출 헤더 객체에 세부 품목 추가 시 양쪽 객체의 참조를 한 번에 연결 처리함
    public void addItem(ExpenseItem item) { // 연관관계 주입 보조 메서드
        this.items.add(item); // 지출 헤더의 세부 품목 리스트에 항목 추가
        item.assignExpense(this); // 세부 품목 객체에 자기 자신(Expense)의 외래키 참조 주입
    }
}