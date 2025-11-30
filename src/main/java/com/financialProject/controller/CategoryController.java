package com.financialProject.controller;

import com.financialProject.dto.CategoryDto;
import com.financialProject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategoryDto = categoryService.saveCategory(categoryDto);
        return  ResponseEntity.status(HttpStatus.OK).body(savedCategoryDto);
    }
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoriesForCurrentUser(){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesForCurrentUser());
    }
    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDto>> getCategoriesByTypeForCurrentUser(@PathVariable String type){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoriesByTypeForCurrentUser(type));
    }
    @PutMapping ("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId ,@RequestBody CategoryDto categoryDto){
        return  ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(categoryDto,categoryId));
    }
}
