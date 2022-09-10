package com.csStudy.CardGame.domain.category.service;

import com.csStudy.CardGame.domain.category.dto.DetailCategory;
import com.csStudy.CardGame.domain.category.dto.NewCategory;
import com.csStudy.CardGame.domain.category.dto.SimpleCategory;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    /* 조회 */
    // 모든 카테고리 조회
    List<SimpleCategory> getAllCategories();

    // 모든 카테고리 상세 조회
    List<DetailCategory> getAllCategoriesDetail();

    // 선택된 카테고리 조회
    List<SimpleCategory> getSelectedCategories(Collection<Long> categoryIdSet);

    // 선택된 카테고리 상세 조회
    List<DetailCategory> getSelectedCategoriesDetail(Collection<Long> categoryIdSet);

    // 특정 카테고리 조회
    SimpleCategory getCategoryById(Long id);

    // 특정 카테고리 상세조회
    DetailCategory getCategoryDetailById(Long id);

    // 카테고리 변경사항(추가, 수정, 삭제) 반영
    boolean changeCategories(List<NewCategory> insertedList, List<SimpleCategory> updatedList, Set<Long> deletedList);
}
