package com.csStudy.CardGame.domain.bookmark.repository;

import com.csStudy.CardGame.domain.bookmark.entity.Bookmark;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<Bookmark> findByMember_Id(UUID memberId, Pageable pageable);
    boolean existsByMemberAndCategory(Member member, Category category);
    Optional<Bookmark> findByMember_IdAndCategoryId(UUID memberId, Long categoryId);
}
