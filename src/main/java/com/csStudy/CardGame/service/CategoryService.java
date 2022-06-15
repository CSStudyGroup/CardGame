package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.CategoryDetail;
import com.csStudy.CardGame.dto.CategoryDto;

import java.util.Collection;
import java.util.List;

public interface CategoryService {
    /* 조회 */
    // 모든 카테고리 조회
    List<CategoryDto> getAllCategories();

    // 모든 카테고리 상세 조회
    List<CategoryDetail> getAllCategoriesDetail();

    // 선택된 카테고리 조회
    List<CategoryDto> getSelectedCategories(Collection<Long> categoryIdSet);

    // 선택된 카테고리 상세 조회
    List<CategoryDetail> getSelectedCategoriesDetail(Collection<Long> categoryIdSet);

    // 특정 카테고리 조회
    CategoryDto getCategoryById(Long id);

    // 특정 카테고리 상세조회
    CategoryDetail getCategoryDetailById(Long id);

}
