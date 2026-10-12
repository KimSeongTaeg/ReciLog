package com.project.recilog.domain; // 도메인 엔티티 패키지 위치

import jakarta.persistence.*; // JPA 어노테이션 전체 임포트
import lombok.*; // Lombok 어노테이션 전체 임포트
import java.math.BigDecimal; // 정밀한 예산 금액 처리를 위한 BigDecimal 임포트

@Entity // JPA 관리 엔티티 선언
@Table(name = "budgets", uniqueConstraints = { // DB "budgets" 테이블 매핑 및 유니크 제약조건
        @UniqueConstraint(name = "uq_user_cat_month", columnNames = {"user_id", "category_id", "target_year_month"}) // 사용자+카테고리+연월 중복 등록 방지
})
@Getter // Getter 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
@AllArgsConstructor // 전체 인자 생성자
@Builder // 빌더 패턴 제공
public class Budget { // 사용자의 월별 카테고리 예산 설정 엔티티

    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "budget_id") // DB 컬럼명
    private Long budgetId; // 예산 ID PK

    @ManyToOne(fetch = FetchType.LAZY) // 예산(N) : 사용자(1) 지연 로딩 매핑
    @JoinColumn(name = "user_id", nullable = false) // FK 컬럼 매핑
    private User user; // 예산을 설정한 회원 참조

    @ManyToOne(fetch = FetchType.LAZY) // 예산(N) : 카테고리(1) 지연 로딩 매핑
    @JoinColumn(name = "category_id", nullable = false) // FK 컬럼 매핑
    private Category category; // 예산 대상 카테고리 참조

    @Column(name = "target_year_month", nullable = false, length = 7) // 예산 목표 연월 (예: "2026-10")
    private String targetYearMonth; // 목표 연월 문자열

    @Column(name = "target_amount", nullable = false, precision = 12, scale = 0) // 목표 예산 금액 (부동 소수점 오차 방지)
    private BigDecimal targetAmount; // 목표 설정 금액

    public void updateAmount(BigDecimal targetAmount) { // 예산 금액 수정 비즈니스 메서드
        this.targetAmount = targetAmount; // 목표 금액 업데이트
    }
}