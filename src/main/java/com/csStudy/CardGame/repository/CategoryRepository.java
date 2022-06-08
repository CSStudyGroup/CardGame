package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository {

    // 카테고리 추가
    void save(Category category);

    // 카테고리 삭제
    int delete(Category category);

    // id로 카테고리 삭제
    int deleteById(Long id);

    // id set 에 포함된 카테고리 삭제
    boolean deleteByIdIn(Set<Long> idList);
    
    // id로 카테고리 검색
    Optional<Category> findOne(Long id);

    // 이름으로 카테고리 검색
    Optional<Category> findByName(String name);

    // id set 에 포함된 카테고리 검색
    Optional<List<Category>> findByIdIn(Set<Long> idSet);

    // 모든 카테고리
    List<Category> findAll();

}
