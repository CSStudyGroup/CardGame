package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // id로 카드를 포함한 카테고리 검색
    @EntityGraph(attributePaths = "cards")
    @Query("select c from Category c where c.id = :id")
    Optional<Category> findDetailOne(@Param("id") Long id);

    // id set 에 포함된 카테고리 검색
    List<Category> findByIdIn(Collection<Long> idSet);

    // id set 에 포함된 카테고리 상세 검색
    @EntityGraph(attributePaths = "cards")
    @Query("select c from Category c where c.id in :idSet")
    List<Category> findDetailByIdIn(@Param("idSet") Collection<Long> idSet);

    // 모든 카테고리 상세
    @EntityGraph(attributePaths = "cards")
    @Query("select c from Category c")
    List<Category> findDetailAll();

}
