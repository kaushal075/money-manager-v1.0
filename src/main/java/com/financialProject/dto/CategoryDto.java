package com.financialProject.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private Long profileId;
    private String name;
    private String icon;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
