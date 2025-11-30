package com.financialProject.services;

import com.financialProject.dto.ExpenseDto;
import com.financialProject.entity.CategoryEntity;
import com.financialProject.entity.ExpenseEntity;
import com.financialProject.entity.ProfileEntity;
import com.financialProject.repository.CategoryRepository;
import com.financialProject.repository.ExpenseRepository;
import com.financialProject.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final ExpenseRepository  expenseRepository;

    public ExpenseDto addExpense(ExpenseDto expenseDto) {
        ProfileEntity profile=profileService.getCurrentProfile();
        CategoryEntity category =  categoryRepository.findById(expenseDto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
        ExpenseEntity newExpense = toEntity(expenseDto, profile, category);
        newExpense = expenseRepository.save(newExpense);
        return toDto(newExpense);
    }

    public List<ExpenseDto> getCurrentMonthExpensesFofCurrentUser(ExpenseDto expenseDto) {
        ProfileEntity profile=profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<ExpenseEntity> entities =expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return entities.stream().map(this::toDto).toList();
    }

    public void deleteExpense(Long expenseId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity entity = expenseRepository.findById(expenseId)
                .orElseThrow(()-> new RuntimeException("Expense not found"));
        if(!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete expense..");
        }
        expenseRepository.delete(entity);

    }
    public List<ExpenseDto> getLatest5ExpensesFForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).toList();
    }

    public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpanseByProfileId(
                profile.getId()
        );
        return total != null ? total : BigDecimal.ZERO;
    }
    public List<ExpenseDto> filterExpense(LocalDate startDate, LocalDate endDate , String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list =expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
        return list.stream().map(this::toDto).toList();
    }
    public List<ExpenseDto> getExpenseForUserOnDate(Long profileId,LocalDate date){
        return expenseRepository.findByProfileIdAndDate(profileId,date).stream().map(this::toDto).toList();
    }

    public ExpenseEntity toEntity(ExpenseDto dto, ProfileEntity profile, CategoryEntity category){
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(category)
                .profile(profile)
                .build();
    }
    public ExpenseDto toDto(ExpenseEntity entity){
        return ExpenseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null )
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName():"N/A")
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
