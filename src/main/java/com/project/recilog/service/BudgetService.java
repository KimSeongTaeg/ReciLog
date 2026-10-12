package com.project.recilog.service; // 서비스 인터페이스 패키지 위치

import com.project.recilog.domain.Budget; // 월별 목표 예산 엔티티 클래스 임포트
import com.project.recilog.dto.BudgetRequestDto; // 예산 설정 요청 DTO 클래스 임포트

import java.util.List; // 리스트 컬렉션 객체 임포트

/**
 * [월별 목표 예산 관리 비즈니스 로직 인터페이스]
 * - 계층 간 결합도를 낮추고(Loose Coupling) 약한 결합을 유지를 위해 인터페이스로 선언함
 */
public interface BudgetService { // 예산 관리 인터페이스 선언

    /**
     * 특정 회원의 특정 카테고리/연월 목표 예산을 설정하거나 수정하는 메서드 (Upsert 기능)
     *
     * @param userId 회원 고유 식별자 PK
     * @param dto    카테고리 ID, 목표 연월(YYYY-MM), 목표 금액이 담긴 DTO
     */
    void saveOrUpdateBudget(Long userId, BudgetRequestDto dto); // 예산 설정 및 수정 비즈니스 메서드 명세

    /**
     * 특정 회원의 특정 목표 연월에 해당하는 전체 카테고리 예산 목록을 조회하는 메서드
     *
     * @param userId         회원 고유 식별자 PK
     * @param targetYearMonth 조회 대상 연월 (예: "2026-10")
     * @return 해당 연월의 설정된 예산 엔티티 리스트
     */
    List<Budget> getMonthlyBudgets(Long userId, String targetYearMonth); // 월별 예산 목록 조회 비즈니스 메서드 명세
}