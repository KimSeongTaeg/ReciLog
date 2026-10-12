package com.project.recilog.dto; // DTO 패키지 위치 선언

import lombok.Builder; // 안전한 객체 구성을 위한 빌더 패턴 어노테이션 임포트
import lombok.Getter; // Getter 자동 생성 어노테이션 임포트
import java.math.BigDecimal; // 정밀한 금액 계산을 위한 BigDecimal 임포트
import java.util.List; // 리스트 컬렉션 객체 임포트

/**
 * [월별 통계 및 예산 소진율 응답 DTO]
 * - 대시보드 화면에서 이번 달 총 지출액, 예산 대비 소진율, 카테고리별 지출 통계 차트 데이터를 전달할 때 사용하는 객체
 */
@Getter // 클라이언트 전송 시 JSON 직렬화를 위한 Getter 자동 생성
@Builder // DTO 객체 생성을 위한 빌더 디자인 패턴 적용
public class MonthlyStatsResponseDto { // 월별 통계 응답 DTO 클래스 선언

    private String targetYearMonth; // 통계 대상 연월 (예: "2026-10")
    private BigDecimal totalExpenseAmount; // 이번 달 누적 총 지출 금액
    private BigDecimal totalBudgetAmount; // 이번 달 설정된 총 목표 예산 금액
    private Double budgetBurnRate; // 예산 대비 지출 소진율 퍼센티지 (예: 78.5%)

    private List<CategoryStatDto> categoryStats; // 카테고리별 지출 금액 및 비율 통계 리스트

    /**
     * [카테고리별 통계 데이터 중첩 DTO]
     * - Recharts 도넛/막대 차트에 시각화하기 위한 카테고리별 통계 단위 객체
     */
    @Getter // 차트 데이터 DTO용 Getter 자동 생성
    @Builder // 차트 데이터 DTO용 빌더 패턴 적용
    public static class CategoryStatDto { // 정적 내부 클래스 선언
        private Integer categoryId; // 카테고리 식별 PK ID
        private String categoryName; // 카테고리 한글 명칭 (예: "식비", "교통")
        private BigDecimal categoryTotalAmount; // 해당 카테고리의 월 누적 지출 금액
        private Double percentage; // 전체 지출 중 해당 카테고리가 차지하는 비율 (%)
    }
}