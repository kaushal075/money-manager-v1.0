package com.financialProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDto {
    private Long id;
    private String name;
    private String icon;
    private String categoryName;
    private Long categoryId;
    private LocalDate date;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
