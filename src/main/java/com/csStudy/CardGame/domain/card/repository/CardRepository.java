package com.csStudy.CardGame.domain.card.repository;

import com.csStudy.CardGame.domain.card.entity.Card;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {

    // 특정 category 에 해당하는 카드 리스트 검색
    List<Card> findByCategory_Id(Long cid);

    // 특정 category 리스트중 하나에 해당하는 카드 리스트 검색
    @EntityGraph(attributePaths = "author")
    List<Card> findByCategory_IdIn(Collection<Long> cidList);

    // 카테고리가 특정 keyword를 포함하는 카드 리스트 검색
    @EntityGraph(attributePaths = "author")
    List<Card> findByCategory_NameContaining(String keyword);

    // 질문이 특정 keyword 를 포함하는 카드 리스트 검색
    @EntityGraph(attributePaths = "author")
    List<Card> findByQuestionContaining(String keyword);

    // 특정 tag 를 포함하는 카드 리스트 검색
    @EntityGraph(attributePaths = "author")
    List<Card> findByTagsContaining(String keyword);

}
