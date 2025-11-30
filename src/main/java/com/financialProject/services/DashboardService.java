package com.financialProject.services;

import com.financialProject.dto.ExpenseDto;
import com.financialProject.dto.IncomeDto;
import com.financialProject.dto.RecentTransactionDto;
import com.financialProject.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProfileService profileService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public Map<String, Object> getDashboardData() {

        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();

        List<IncomeDto> latestIncomes = incomeService.getLatest5incomesFOrCurrentUser();
        List<ExpenseDto> latestExpenses = expenseService.getLatest5ExpensesFForCurrentUser();

        // Convert incomes to RecentTransactionDto
        List<RecentTransactionDto> incomeTransactions = latestIncomes.stream()
                .map(income -> RecentTransactionDto.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .name(income.getName())
                        .icon(income.getIcon())
                        .amount(income.getAmount())

                        .date(income.getDate() != null
                                ? income.getDate()
                                : income.getCreatedAt().toLocalDate())

                        .createdAt(income.getCreatedAt())
                        .updatedAt(income.getUpdatedAt())
                        .type("income")
                        .build())
                .collect(Collectors.toList());

        List<RecentTransactionDto> expenseTransactions = latestExpenses.stream()
                .map(expense -> RecentTransactionDto.builder()
                        .id(expense.getId())
                        .profileId(profile.getId())
                        .name(expense.getName())
                        .icon(expense.getIcon())
                        .amount(expense.getAmount())
                        .date(expense.getDate())
                        .createdAt(expense.getCreatedAt())
                        .updatedAt(expense.getUpdatedAt())
                        .type("expense")
                        .build())
                .collect(Collectors.toList());

        List<RecentTransactionDto> transactions =
                concat(incomeTransactions.stream(), expenseTransactions.stream())
                        .sorted((a, b) -> {

                            LocalDate dateA = a.getDate();
                            LocalDate dateB = b.getDate();

                            int cmp = dateB.compareTo(dateA);

                            if (cmp == 0 &&
                                    a.getCreatedAt() != null &&
                                    b.getCreatedAt() != null) {
                                return b.getCreatedAt().compareTo(a.getCreatedAt());
                            }

                            return cmp;
                        })
                        .collect(Collectors.toList());

        var totalIncome = incomeService.getTotalIncomeForCurrentUser();
        var totalExpenses = expenseService.getTotalExpenseForCurrentUser();

        returnValue.put("totalBalance", totalIncome.subtract(totalExpenses));
        returnValue.put("totalIncome", totalIncome);
        returnValue.put("totalExpenses", totalExpenses);
        returnValue.put("recent5Incomes", latestIncomes);
        returnValue.put("recent5Expenses", latestExpenses);
        returnValue.put("recentTransaction", transactions);

        return returnValue;
    }
}
