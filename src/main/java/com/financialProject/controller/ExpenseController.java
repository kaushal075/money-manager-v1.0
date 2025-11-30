package com.financialProject.controller;

import com.financialProject.dto.ExpenseDto;
import com.financialProject.repository.ExpenseRepository;
import com.financialProject.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody ExpenseDto expenseDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.addExpense(expenseDto));
    }
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable long expenseId){
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }


}
