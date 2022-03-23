package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    // id로 카테고리 검색
    Optional<Category> findById(int id);

    // 이름으로 카테고리 검색
    Optional<Category> findByName(String name);

    // 모든 카테고리
    List<Category> findAll();

    // 카테고리 추가
    Category insert(Category category);

    // 카테고리 삭제
    int delete(Category category);

    // 카테고리 수정
    int update(Category category);

}
