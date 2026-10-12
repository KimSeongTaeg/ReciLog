package com.project.recilog.repository; // 리포지토리 패키지 위치 선언

import com.project.recilog.domain.Budget; // 목표 예산 엔티티 클래스 임포트
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPA 기본 인터페이스 임포트
import java.util.List; // 리스트 컬렉션 임포트
import java.util.Optional; // Null-Safe한 단건 조회를 위한 Optional 임포트

/**
 * [월별 목표 예산(Budget) 엔티티 전담 JPA 리포지토리 인터페이스]
 * - 사용자별, 카테고리별, 연월별 예산 설정 정보 조회 쿼리 제공
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> { // 엔티티 타입 Budget, PK 타입 Long 지정

    /**
     * [쿼리 메서드] 특정 회원의 특정 카테고리 및 특정 연월에 설정된 예산 단건 조회
     * - 예산 설정 시 기존 설정값이 존재하는지 확인하여 업데이트(Upsert)하기 위해 사용됨
     *
     * @param userId          회원 고유 식별자 PK
     * @param categoryId      카테고리 고유 식별자 PK
     * @param targetYearMonth 목표 연월 문자열 (예: "2026-10")
     * @return 해당 연월/카테고리의 예산 단건 정보를 담은 Optional 객체
     */
    Optional<Budget> findByUserUserIdAndCategoryCategoryIdAndTargetYearMonth(Long userId, Integer categoryId, String targetYearMonth);

    /**
     * [쿼리 메서드] 특정 회원의 특정 연월 전체 설정 예산 리스트 조회
     * - 대시보드 화면에서 이번 달 전체 카테고리의 설정 예산 현황을 출력할 때 사용함
     *
     * @param userId          회원 고유 식별자 PK
     * @param targetYearMonth 목표 연월 문자열 (예: "2026-10")
     * @return 해당 월에 설정된 전체 예산 엔티티 리스트
     */
    List<Budget> findByUserUserIdAndTargetYearMonth(Long userId, String targetYearMonth);
}