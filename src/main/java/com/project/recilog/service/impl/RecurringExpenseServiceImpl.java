package com.project.recilog.service.impl; // 서비스 구현체 패키지 위치

import com.project.recilog.domain.Category; // 카테고리 엔티티
import com.project.recilog.domain.RecurringExpense; // 고정 지출 엔티티
import com.project.recilog.domain.User; // 회원 엔티티
import com.project.recilog.dto.RecurringExpenseRequestDto; // 고정지출 DTO
import com.project.recilog.repository.CategoryRepository; // 카테고리 DB
import com.project.recilog.repository.RecurringExpenseRepository; // 고정지출 DB
import com.project.recilog.repository.UserRepository; // 회원 DB
import com.project.recilog.service.RecurringExpenseService; // 인터페이스
import lombok.RequiredArgsConstructor; // 생성자 주입
import org.springframework.stereotype.Service; // 서비스 컴포넌트
import org.springframework.transaction.annotation.Transactional; // 트랜잭션

import java.util.List; // 리스트

@Service // 서비스 빈 등록
@RequiredArgsConstructor // 생성자 주입
@Transactional(readOnly = true) // 읽기 전용 트랜잭션
public class RecurringExpenseServiceImpl implements RecurringExpenseService { // 고정 지출 서비스 구현체

    private final RecurringExpenseRepository recurringExpenseRepository; // 고정지출 DB 주입
    private final CategoryRepository categoryRepository; // 카테고리 DB 주입
    private final UserRepository userRepository; // 회원 DB 주입

    @Override // 고정 지출 신규 등록
    @Transactional // 쓰기 트랜잭션
    public void saveRecurringExpense(Long userId, RecurringExpenseRequestDto dto) {
        User user = userRepository.findById(userId) // 회원 조회
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Category category = categoryRepository.findById(dto.getCategoryId()) // 카테고리 조회
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        RecurringExpense recurringExpense = RecurringExpense.builder() // 고정지출 엔티티 빌드
                .user(user)
                .category(category)
                .storeName(dto.getStoreName())
                .amount(dto.getAmount())
                .paymentDay(dto.getPaymentDay())
                .paymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod() : "CARD")
                .memo(dto.getMemo())
                .isActive(true)
                .build();

        recurringExpenseRepository.save(recurringExpense); // DB 저장
    }

    @Override // 활성화된 고정 지출 목록 조회
    public List<RecurringExpense> getActiveRecurringExpenses(Long userId) {
        return recurringExpenseRepository.findByUserUserIdAndIsActiveTrue(userId); // DB 조회 반환
    }
}