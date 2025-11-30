package com.financialProject.services;


import com.financialProject.dto.IncomeDto;
import com.financialProject.entity.CategoryEntity;
import com.financialProject.entity.IncomeEntity;
import com.financialProject.entity.ProfileEntity;
import com.financialProject.repository.CategoryRepository;
import com.financialProject.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final IncomeRepository incomeRepository;

    public IncomeDto addIncome(IncomeDto incomeDto) {
        ProfileEntity profile=profileService.getCurrentProfile();
        CategoryEntity category =  categoryRepository.findById(incomeDto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found"));
        IncomeEntity newIncome = toEntity(incomeDto, profile, category);
        newIncome = incomeRepository.save(newIncome);
        return toDto(newIncome);
    }

    public List<IncomeDto> getCurrentMonthIncomesForCurrentUser(IncomeDto incomeDto){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate  = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDto).toList();
    }

    public void deleteIncome(Long incomeId){
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
                .orElseThrow(()-> new RuntimeException("Income not found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to dele income..");
        }
        incomeRepository.delete(entity);
    }
    public List<IncomeDto> getLatest5incomesFOrCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).toList();
    }
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalExpanseByProfileId(
                profile.getId()
        );
        return total != null ? total : BigDecimal.ZERO;
    }
    public List<IncomeDto> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository
                .findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                        profile.getId(),
                        startDate,
                        endDate,
                        keyword,
                        sort
                );
        return list.stream().map(this::toDto).toList();
    }




    public IncomeEntity toEntity(IncomeDto dto, ProfileEntity profileEntity, CategoryEntity categoryEntity){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(categoryEntity)
                .profile(profileEntity)
                .build();
    }


    public IncomeDto toDto(IncomeEntity incomeEntity) {
        return IncomeDto.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getName())
                .icon(incomeEntity.getIcon())
                .amount(incomeEntity.getAmount())
                .categoryId(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getId() : null)
                .categoryName(incomeEntity.getCategory() !=null ? incomeEntity.getCategory().getName() : "N/A")
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();

    }

}
