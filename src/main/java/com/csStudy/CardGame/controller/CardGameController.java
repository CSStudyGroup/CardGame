package com.csStudy.CardGame.controller;

import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.dto.CategoryDetail;
import com.csStudy.CardGame.service.CardService;
import com.csStudy.CardGame.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 기존 매핑 REST API 로 리팩토링
@RestController
@RequestMapping("/cardgame/*")
public class CardGameController {

    private final CategoryService categoryService;

    @Autowired
    public CardGameController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    // 전체 카테고리 또는 선택된 카테고리 리스트를 반환하는 API
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam List<Long> selected) {
        if (selected == null) {
            return categoryService.getAllCategories();
        }
        else {
            return categoryService.getSelectedCategories(selected);
        }
    }

    // 특정 카테고리를 반환하는 API
    @GetMapping("/categories/{cid}")
    public CategoryDto getCategory(@PathVariable Long cid) {
        return categoryService.getCategoryById(cid);
    }

    // 전체 카테고리 또는 선택된 카테고리 상세조회 리스트를 반환하는 API
    @GetMapping("/categories/detail")
    public List<CategoryDetail> getCategoriesDetail(@RequestParam List<Long> selected) {
        if (selected == null) {
            return categoryService.getAllCategoriesDetail();
        }
        else {
            return categoryService.getSelectedCategoriesDetail(selected);
        }
    }

    // 특정 카테고리 상세 조회 API
    @GetMapping("/categories/detail/{cid}")
    public CategoryDetail getCategoryDetail(@PathVariable Long cid) {
        return categoryService.getCategoryDetailById(cid);
    }

}
