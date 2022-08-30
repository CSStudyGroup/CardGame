package com.csStudy.CardGame.domain.category.controller;

import com.csStudy.CardGame.domain.category.dto.NewCategory;
import com.csStudy.CardGame.domain.category.dto.SimpleCategory;
import com.csStudy.CardGame.domain.category.dto.CategoryDetail;
import com.csStudy.CardGame.domain.category.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<List<SimpleCategory>> getCategories(@RequestParam(required = false) List<Long> selected) {
        if (selected == null) {
            return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(categoryService.getSelectedCategories(selected), HttpStatus.OK);
        }
    }

    // 특정 카테고리를 반환하는 API
    @GetMapping("/categories/{cid}")
    public SimpleCategory getCategory(@PathVariable Long cid) {
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

    // 카테고리 변경사항 반영 API
    @PostMapping("/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean changeCategories(@RequestBody String jsonList) throws JsonProcessingException {
        JSONObject jObject = new JSONObject(jsonList);
        ObjectMapper mapper = new ObjectMapper();

        // 추가
        List<NewCategory> insertedCategoryList = new ArrayList<>();
        JSONArray insertList = jObject.getJSONArray("insert");
        for (Object o: insertList) {
            NewCategory newCategory = mapper.readValue(o.toString(), NewCategory.class);
            insertedCategoryList.add(newCategory);
        }

        // 수정
        List<SimpleCategory> updatedCategoryList = new ArrayList<>();
        JSONArray updateList = jObject.getJSONArray("update");
        for (Object o: updateList) {
            SimpleCategory dto = mapper.readValue(o.toString(), SimpleCategory.class);
            updatedCategoryList.add(dto);
        }

        // 삭제
        Set<Long> deletedCategorySet = new HashSet<>();
        JSONArray deleteList = jObject.getJSONArray("delete");
        for (Object o: deleteList) {
            SimpleCategory dto = mapper.readValue(o.toString(), SimpleCategory.class);
            deletedCategorySet.add(dto.getId());
        }

        return categoryService.changeCategories(insertedCategoryList, updatedCategoryList, deletedCategorySet);
    }

}
