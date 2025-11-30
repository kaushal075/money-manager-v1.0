package com.financialProject.controller;

import com.financialProject.dto.IncomeDto;
import com.financialProject.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto incomeDto){

        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.addIncome(incomeDto));

    }
    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteIncome(@PathVariable long incomeId){
        incomeService.deleteIncome(incomeId);
        return ResponseEntity.noContent().build();
    }
}
