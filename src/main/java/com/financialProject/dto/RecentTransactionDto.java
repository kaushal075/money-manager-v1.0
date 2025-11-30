package com.financialProject.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentTransactionDto {
    private Long id;
    private Long profileId;
    private String icon;
    private String name;
    private BigDecimal amount;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String type;
}
