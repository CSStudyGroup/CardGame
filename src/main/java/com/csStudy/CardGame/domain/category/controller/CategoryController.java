package com.csStudy.CardGame.domain.category.controller;

import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;
import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithOwnerInfo;
import com.csStudy.CardGame.domain.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    /* 카테고리 관련 API */

    // 전체 카테고리 또는 선택된 카테고리 리스트를 반환하는 API
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(required = false) List<Long> selected) {
        if (selected == null) {
            return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(categoryService.getSelectedCategories(selected), HttpStatus.OK);
        }
    }

    // 특정 카테고리를 반환하는 API
    @GetMapping("/categories/{cid}")
    public CategoryDto getCategory(@PathVariable Long cid) {
        return categoryService.getCategoryById(cid);
    }

    // 전체 카테고리 또는 선택된 카테고리 상세조회 리스트를 반환하는 API
    @GetMapping("/categories/detail")
    public List<CategoryDtoWithOwnerInfo> getCategoriesDetail(@RequestParam List<Long> selected) {
        if (selected == null) {
            return categoryService.getAllCategoriesDetail();
        }
        else {
            return categoryService.getSelectedCategoriesDetail(selected);
        }
    }

    // 특정 카테고리 상세 조회 API
    @GetMapping("/categories/detail/{cid}")
    public CategoryDtoWithOwnerInfo getCategoryDetail(@PathVariable Long cid) {
        return categoryService.getCategoryDetailById(cid);
    }

    //======================================

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> addCategory(@RequestBody NewCategoryForm newCategoryForm) {
        CategoryDto addedCategory = categoryService.addCategory(newCategoryForm);

        URI location = ServletUriComponentsBuilder
                .fromUriString("/categories")
                .path("/{id}")
                .buildAndExpand(addedCategory.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @PutMapping("/categories")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> editCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.editCategory(categoryDto);
        return ResponseEntity.ok().build();
    }

}
