package com.financialProject.dto;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FilterDto {
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyword;
    private String sortField;// date ,name,amount
    private String sortOrder;//asc - desc
}
