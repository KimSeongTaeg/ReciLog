package com.project.recilog.dto;

import com.project.recilog.domain.Expense;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ExpenseResponseDto {

    private Long expenseId;
    private Integer categoryId;
    private String categoryName;
    private String storeName;
    private BigDecimal totalAmount;
    private LocalDateTime spentAt;
    private String paymentMethod;
    private String memo;
    private List<ExpenseItemResponseDto> items;

    @Getter
    @Builder
    public static class ExpenseItemResponseDto {
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotalPrice;
    }

    public static ExpenseResponseDto fromEntity(Expense entity) {
        return ExpenseResponseDto.builder()
                .expenseId(entity.getExpenseId())
                .categoryId(entity.getCategory().getCategoryId())
                .categoryName(entity.getCategory().getName())
                .storeName(entity.getStoreName())
                .totalAmount(entity.getTotalAmount())
                .spentAt(entity.getSpentAt())
                .paymentMethod(entity.getPaymentMethod())
                .memo(entity.getMemo())
                .items(entity.getItems().stream()
                        .map(i -> ExpenseItemResponseDto.builder()
                                .itemId(i.getItemId())
                                .itemName(i.getItemName())
                                .quantity(i.getQuantity())
                                .unitPrice(i.getUnitPrice())
                                .subtotalPrice(i.getSubtotalPrice())
                                .build())
                        .toList())
                .build();
    }
}