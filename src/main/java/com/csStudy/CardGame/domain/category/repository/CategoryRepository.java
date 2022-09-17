package com.csStudy.CardGame.domain.category.repository;

import com.csStudy.CardGame.domain.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = "owner")
    @Query("select c from Category c where c.id = :id")
    Optional<Category> findByIdWithDetail(@Param("id") Long id);

    Page<Category> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "owner")
    @Query("select c from Category c order by c.id asc")
    Page<Category> findAllWithDetail(Pageable pageable);

    Page<Category> findByOwner_Id(UUID ownerId, Pageable pageable);

    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @EntityGraph(attributePaths = "owner")
    @Query("select c from Category c where lower(c.name) like %:keyword% escape '%' order by c.id asc")
    Page<Category> findByNameContainingIgnoreCaseWithDetail(@Param("keyword") String keyword, Pageable pageable);

    Page<Category> findByOwner_IdIsAndNameContainingIgnoreCase(UUID ownerId, String keyword, Pageable pageable);
}
