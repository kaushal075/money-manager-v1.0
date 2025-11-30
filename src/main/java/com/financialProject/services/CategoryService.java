package com.financialProject.services;

import com.financialProject.dto.CategoryDto;
import com.financialProject.entity.CategoryEntity;
import com.financialProject.entity.ProfileEntity;
import com.financialProject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;



    public CategoryDto saveCategory(CategoryDto categoryDto){
        ProfileEntity profile = profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDto.getName(), profile.getId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category already exists");

        }

        CategoryEntity newCategory = toEntity(categoryDto, profile);
        newCategory = categoryRepository.save(newCategory);
        return toDto(newCategory);

    }

    public List<CategoryDto> getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDto).toList();
    }
    public List<CategoryDto> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities =categoryRepository.findByTypeAndProfileId(type,profile.getId());
        return entities.stream().map(this::toDto).toList();
    }

    public CategoryDto updateCategory(CategoryDto categoryDto,Long categoryId){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategories = categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
                .orElseThrow(() ->new RuntimeException("Category not found"));
        existingCategories.setName(categoryDto.getName());
        existingCategories.setIcon(categoryDto.getIcon());
        existingCategories.setType(categoryDto.getType());

        existingCategories = categoryRepository.save(existingCategories);
        return toDto(existingCategories);


    }



    private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profile) {
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .profile(profile)
                .type(categoryDto.getType())
                .build();

    }
    private CategoryDto toDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .profileId((categoryEntity.getProfile() !=null ? categoryEntity.getProfile().getId():null))
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .updatedAt(categoryEntity.getUpdatedAt())
                .createdAt(categoryEntity.getCreatedAt())
                .build();
    }

}
