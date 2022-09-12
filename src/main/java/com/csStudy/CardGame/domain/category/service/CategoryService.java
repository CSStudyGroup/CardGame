package com.csStudy.CardGame.domain.category.service;

import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithOwnerInfo;
import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;

import java.util.Collection;
import java.util.List;

public interface CategoryService {
    /* 조회 */
    // 모든 카테고리 조회
    List<CategoryDto> getAllCategories();

    // 모든 카테고리 상세 조회
    List<CategoryDtoWithOwnerInfo> getAllCategoriesDetail();

    // 선택된 카테고리 조회
    List<CategoryDto> getSelectedCategories(Collection<Long> categoryIdSet);

    // 선택된 카테고리 상세 조회
    List<CategoryDtoWithOwnerInfo> getSelectedCategoriesDetail(Collection<Long> categoryIdSet);

    // 특정 카테고리 조회
    CategoryDto getCategoryById(Long id);

    // 특정 카테고리 상세조회
    CategoryDtoWithOwnerInfo getCategoryDetailById(Long id);

    //==================

    // 카테고리 추가
    CategoryDto addCategory(NewCategoryForm newCategoryForm);

    // 카테고리 수정
    void editCategory(CategoryDto categoryDto);
}
