package com.project.recilog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ExpenseRequestDto {

    @NotNull
    private Integer categoryId;

    @NotBlank
    private String storeName;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private LocalDateTime spentAt;

    private String paymentMethod;
    private String memo;

    private List<ExpenseItemDto> items;

    @Getter
    @NoArgsConstructor
    public static class ExpenseItemDto {
        @NotBlank
        private String itemName;
        @NotNull
        private Integer quantity;
        @NotNull
        private BigDecimal unitPrice;
        @NotNull
        private BigDecimal subtotalPrice;
    }
}