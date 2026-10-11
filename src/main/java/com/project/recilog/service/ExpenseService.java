package com.project.recilog.service;

import com.project.recilog.dto.ExpenseRequestDto;
import com.project.recilog.dto.ExpenseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {
    ExpenseResponseDto saveExpense(Long userId, ExpenseRequestDto dto);
    Page<ExpenseResponseDto> getExpenses(Long userId, Integer categoryId, String keyword, Pageable pageable);
    byte[] exportToExcel(Long userId);
}