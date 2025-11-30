package com.financialProject.controller;

import com.financialProject.dto.ExpenseDto;
import com.financialProject.dto.FilterDto;
import com.financialProject.dto.IncomeDto;
import com.financialProject.services.ExpenseService;
import com.financialProject.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?>  filterTransactions(@RequestBody FilterDto filterDto) {
        LocalDate stratDate = filterDto.getStartDate() != null? filterDto.getStartDate(): LocalDate.MIN;
        LocalDate endDate = filterDto.getEndDate() != null? filterDto.getEndDate(): LocalDate.now();
        String keyword = filterDto.getKeyword() != null? filterDto.getKeyword(): "";
        String sortField = filterDto.getSortField() != null? filterDto.getSortField(): "date";
        Sort.Direction direction ="desc".equalsIgnoreCase(filterDto.getSortOrder())?Sort.Direction.DESC:Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);
        if("income".equals(filterDto.getType())){
           List<IncomeDto> list = incomeService.filterIncomes(stratDate,endDate,keyword,sort);
           return ResponseEntity.ok().body(list);
        }
        if("expense".equals(filterDto.getType())){
            List<ExpenseDto> expense = expenseService.filterExpense(stratDate,endDate,keyword,sort);
            return ResponseEntity.ok().body(expense);
        }else{
            return ResponseEntity.badRequest().body("Invalid Type must be income or expense");
        }

    }
}
