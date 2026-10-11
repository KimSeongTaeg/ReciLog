package com.project.recilog.service.impl;

import com.project.recilog.domain.Category;
import com.project.recilog.domain.Expense;
import com.project.recilog.domain.ExpenseItem;
import com.project.recilog.domain.User;
import com.project.recilog.dto.ExpenseRequestDto;
import com.project.recilog.dto.ExpenseResponseDto;
import com.project.recilog.repository.CategoryRepository;
import com.project.recilog.repository.ExpenseRepository;
import com.project.recilog.repository.UserRepository;
import com.project.recilog.service.ExpenseService;
import com.project.recilog.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExcelUtil excelUtil;

    @Override
    @Transactional
    public ExpenseResponseDto saveExpense(Long userId, ExpenseRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Expense expense = Expense.builder()
                .user(user)
                .category(category)
                .storeName(dto.getStoreName())
                .totalAmount(dto.getTotalAmount())
                .spentAt(dto.getSpentAt())
                .paymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod() : "CARD")
                .memo(dto.getMemo())
                .build();

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            dto.getItems().forEach(itemDto -> {
                ExpenseItem item = ExpenseItem.builder()
                        .itemName(itemDto.getItemName())
                        .quantity(itemDto.getQuantity())
                        .unitPrice(itemDto.getUnitPrice())
                        .subtotalPrice(itemDto.getSubtotalPrice())
                        .build();
                expense.addItem(item);
            });
        }

        Expense saved = expenseRepository.save(expense);
        return ExpenseResponseDto.fromEntity(saved);
    }

    @Override
    public Page<ExpenseResponseDto> getExpenses(Long userId, Integer categoryId, String keyword, Pageable pageable) {
        return expenseRepository.searchExpenses(userId, categoryId, keyword, pageable)
                .map(ExpenseResponseDto::fromEntity);
    }

    @Override
    public byte[] exportToExcel(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserUserIdAndSpentAtBetween(
                userId,
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2099, 12, 31, 23, 59)
        );
        return excelUtil.createExpenseExcel(expenses);
    }
}