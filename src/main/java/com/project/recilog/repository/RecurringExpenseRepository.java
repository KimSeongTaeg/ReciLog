package com.project.recilog.repository; // 리포지토리 패키지 위치

import com.project.recilog.domain.RecurringExpense; // 고정지출 엔티티
import org.springframework.data.jpa.repository.JpaRepository; // JPA 인터페이스
import java.util.List; // 리스트

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Long> { // 고정 지출 DB 처리
    // 특정 회원의 활성화 상태 고정 지출 전체 리스트 조회
    List<RecurringExpense> findByUserUserIdAndIsActiveTrue(Long userId);
}