package com.csStudy.CardGame.domain.card.repository;

import com.csStudy.CardGame.domain.card.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {

    // 특정 category 에 해당하는 카드 리스트 검색
    Page<Card> findByCategory_IdOrderByIdAsc(Long categoryId, Pageable pageable);

    // 질문이 특정 keyword 를 포함하는 카드 리스트 검색
    List<Card> findByQuestionContaining(String keyword);

}
