package com.csStudy.CardGame.domain.category.controller;

import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;
import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithDetail;
import com.csStudy.CardGame.domain.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long categoryId) {
        return ResponseEntity
                .ok()
                .body(categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/categories/detail/{categoryId}")
    public ResponseEntity<CategoryDtoWithDetail> getCategoryWithDetail(@PathVariable Long categoryId) {
        return ResponseEntity
                .ok()
                .body(categoryService.getCategoryWithDetail(categoryId));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 50) Pageable pageable
    ) {
        return ResponseEntity
                .ok()
                .body(
                        (keyword == null) ?
                                categoryService.getAllCategories(pageable)
                                : categoryService.getCategories(keyword, pageable)
                );
    }

    @GetMapping("/categories/detail")
    public ResponseEntity<List<CategoryDtoWithDetail>> getCategoriesWithDetail(
            @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 50) Pageable pageable
    ) {
        return ResponseEntity
                .ok()
                .body(
                        (keyword == null) ?
                                categoryService.getAllCategoriesWithDetail(pageable)
                                : categoryService.getCategoriesWithDetail(keyword, pageable)
                );
    }
}
