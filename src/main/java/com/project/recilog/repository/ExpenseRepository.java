package com.project.recilog.repository;

import com.project.recilog.domain.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @EntityGraph(attributePaths = {"category"})
    @Query("SELECT e FROM Expense e WHERE " +
            "e.user.userId = :userId AND " +
            "e.isDeleted = false AND " +
            "(:categoryId IS NULL OR e.category.categoryId = :categoryId) AND " +
            "(:keyword IS NULL OR :keyword = '' OR e.storeName LIKE %:keyword% OR e.memo LIKE %:keyword%) " +
            "ORDER BY e.spentAt DESC")
    Page<Expense> searchExpenses(@Param("userId") Long userId,
                                 @Param("categoryId") Integer categoryId,
                                 @Param("keyword") String keyword,
                                 Pageable pageable);

    @EntityGraph(attributePaths = {"category"})
    @Query("SELECT e FROM Expense e WHERE e.user.userId = :userId AND e.isDeleted = false AND e.spentAt BETWEEN :start AND :end")
    List<Expense> findByUserUserIdAndSpentAtBetween(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}