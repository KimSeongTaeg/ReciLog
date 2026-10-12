package com.project.recilog.service.impl; // 서비스 구현체 패키지 위치

import com.project.recilog.domain.Budget; // 예산 엔티티
import com.project.recilog.domain.Category; // 카테고리 엔티티
import com.project.recilog.domain.User; // 회원 엔티티
import com.project.recilog.dto.BudgetRequestDto; // 예산 DTO
import com.project.recilog.repository.BudgetRepository; // 예산 리포지토리
import com.project.recilog.repository.CategoryRepository; // 카테고리 리포지토리
import com.project.recilog.repository.UserRepository; // 회원 리포지토리
import com.project.recilog.service.BudgetService; // 서비스 인터페이스
import lombok.RequiredArgsConstructor; // 생성자 주입
import org.springframework.stereotype.Service; // 서비스 컴포넌트
import org.springframework.transaction.annotation.Transactional; // 트랜잭션 어노테이션

import java.util.List; // 리스트

@Service // Spring 서비스 빈 등록
@RequiredArgsConstructor // final 필드 자동 주입
@Transactional(readOnly = true) // 읽기 전용 기본 트랜잭션
public class BudgetServiceImpl implements BudgetService { // 예산 관리 서비스 구현체

    private final BudgetRepository budgetRepository; // 예산 DB 주입
    private final CategoryRepository categoryRepository; // 카테고리 DB 주입
    private final UserRepository userRepository; // 회원 DB 주입

    @Override // 예산 설정/수정 메서드 구현
    @Transactional // CUD 작성을 위한 쓰기 트랜잭션 적용
    public void saveOrUpdateBudget(Long userId, BudgetRequestDto dto) { // 예산 업서트 비즈니스 로직
        User user = userRepository.findById(userId) // 회원 조회
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")); // 예외 발생
        Category category = categoryRepository.findById(dto.getCategoryId()) // 카테고리 조회
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다.")); // 예외 발생

        // 이미 기존 연월/카테고리에 설정된 예산이 있는지 DB 조회
        Budget budget = budgetRepository.findByUserUserIdAndCategoryCategoryIdAndTargetYearMonth(
                        userId, dto.getCategoryId(), dto.getTargetYearMonth())
                .orElse(null); // 없으면 null

        if (budget != null) { // 기존 예산 데이터가 존재하면
            budget.updateAmount(dto.getTargetAmount()); // Dirty Checking(변경 감지)을 활용한 금액 업데이트
        } else { // 기존 예산 데이터가 없으면 신규 생성
            budget = Budget.builder()
                    .user(user)
                    .category(category)
                    .targetYearMonth(dto.getTargetYearMonth())
                    .targetAmount(dto.getTargetAmount())
                    .build();
            budgetRepository.save(budget); // DB 신규 INSERT 저장
        }
    }

    @Override // 특정 연월 목표 예산 조회
    public List<Budget> getMonthlyBudgets(Long userId, String targetYearMonth) {
        return budgetRepository.findByUserUserIdAndTargetYearMonth(userId, targetYearMonth); // DB 조회 후 결과 반환
    }
}