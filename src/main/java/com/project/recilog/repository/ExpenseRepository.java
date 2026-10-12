package com.project.recilog.repository; // 리포지토리 패키지 위치 선언

import com.project.recilog.domain.Expense; // 지출 엔티티 클래스 임포트
import org.springframework.data.domain.Page; // 페이징 처리된 데이터와 페이지 정보를 담는 Spring Data 인터페이스 임포트
import org.springframework.data.domain.Pageable; // 페이지 번호, 페이지 크기, 정렬 정보를 전달하는 인메모리 객체 임포트
import org.springframework.data.jpa.repository.EntityGraph; // 연관 엔티티 Fetch JOIN 처리를 위한 JPA 어노테이션 임포트
import org.springframework.data.jpa.repository.JpaRepository; // Spring Data JPA 기본 인터페이스 임포트
import org.springframework.data.jpa.repository.Query; // 객체지향 쿼리 언어(JPQL) 작성을 위한 어노테이션 임포트
import org.springframework.data.repository.query.Param; // JPQL 내부 변수와 바인딩할 매개변수 지정 어노테이션 임포트

import java.time.LocalDateTime; // 날짜 및 시간 검색 범위를 위한 Java8 객체 임포트
import java.util.List; // 데이터 목록 반환용 List 컬렉션 임포트

/**
 * [지출(Expense) 엔티티 전담 JPA 리포지토리 인터페이스]
 * - 1:N 구조의 데이터 동적 검색, 페이징, N+1 연산 최적화 Fetch JOIN 쿼리 제공
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> { // 엔티티 타입 Expense, PK 타입 Long 지정

    /**
     * [성능 최적화 동적 검색 및 페이징 메서드]
     * - @EntityGraph: 지연 로딩(LAZY)으로 설정된 category 엔티티를 한 번의 SQL JOIN으로 함께 가져와 N+1 문제 해결
     * - @Query(JPQL): 엔티티 객체를 대상으로 작성하는 객체지향 SQL 쿼리
     *
     * @param userId     현재 접속한 로그인 회원의 ID (인증 보안용 필수 필터)
     * @param categoryId 조회할 카테고리 ID (NULL 전달 시 카테고리 검색 조건 무시)
     * @param keyword    상호명 또는 메모에 포함될 검색어 (NULL 또는 빈 문자열 시 키워드 조건 무시)
     * @param pageable   오프셋 기반 페이징 정보 (page, size, sort 정보 포함)
     * @return 페이징 결과(내용물 List, 총 페이지 수, 전체 데이터 개수 등)를 포함한 Page<Expense> 객체
     */
    @EntityGraph(attributePaths = {"category"}) // 연관 엔티티인 category를 Fetch JOIN하여 가져옴 (N+1 최적화)
    @Query("SELECT e FROM Expense e WHERE " + // JPQL 구문 시작 (e는 Expense 엔티티 별칭)
            "e.user.userId = :userId AND " + // 1. 현재 사용자의 데이터만 엄격하게 필터링 (보안)
            "e.isDeleted = false AND " + // 2. 논리적 삭제(Soft Delete)가 적용되지 않은 활성 데이터만 조회
            "(:categoryId IS NULL OR e.category.categoryId = :categoryId) AND " + // 3. 카테고리 ID 동적 파라미터 조건 처리
            "(:keyword IS NULL OR :keyword = '' OR e.storeName LIKE %:keyword% OR e.memo LIKE %:keyword%) " + // 4. 상호명/메모 LIKE 검색 조건
            "ORDER BY e.spentAt DESC") // 5. 가장 최근 결제된 일시부터 내림차순 정렬
    Page<Expense> searchExpenses(@Param("userId") Long userId, // 파라미터 매핑: 사용자 ID
                                 @Param("categoryId") Integer categoryId, // 파라미터 매핑: 카테고리 ID
                                 @Param("keyword") String keyword, // 파라미터 매핑: 검색 키워드
                                 Pageable pageable); // 페이징 객체 매핑

    /**
     * [날짜 범위 지출 내역 조회 메서드 (엑셀 추출용)]
     * - 특정 기간 사이의 사용자의 지출 내역을 Fetch JOIN으로 한 번에 묶어서 조회
     *
     * @param userId 로그인 회원 ID
     * @param start  조회 시작 일시 (예: 2000-01-01 00:00:00)
     * @param end    조회 종료 일시 (예: 2099-12-31 23:59:59)
     * @return 기간 내의 전체 지출 엔티티 리스트
     */
    @EntityGraph(attributePaths = {"category"}) // 엑셀 변환 시 카테고리명을 사용하므로 N+1 방지를 위해 Fetch JOIN
    @Query("SELECT e FROM Expense e WHERE e.user.userId = :userId AND e.isDeleted = false AND e.spentAt BETWEEN :start AND :end")
    List<Expense> findByUserUserIdAndSpentAtBetween(@Param("userId") Long userId, // 파라미터 매핑: 사용자 ID
                                                    @Param("start") LocalDateTime start, // 파라미터 매핑: 시작 일시
                                                    @Param("end") LocalDateTime end); // 파라미터 매핑: 종료 일시
}