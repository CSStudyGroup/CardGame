package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Card;

import java.util.List;
import java.util.Optional;


public interface CardRepository {
    // 카드 추가
    Optional<Card> save(Card card);

    // 카드 삭제
    int delete(Card card);

    int deleteById(Long id);

    // id로 카드 검색
    Optional<Card> findOne(Long id);

    // 모든 카드
    List<Card> findAll();

    // 특정 category 에 해당하는 카드 리스트 검색
    List<Card> findByCategoryId(Long cid);

    // 특정 category 리스트중 하나에 해당하는 카드 리스트 검색
    List<Card> findByCategoryIn(List<Long> cidList);

    // 카테고리가 특정 keyword를 포함하는 카드 리스트 검색
    List<Card> findByCategoryNameContaining(String keyword);

    // 질문이 특정 keyword 를 포함하는 카드 리스트 검색
    List<Card> findByQuestionContaining(String keyword);

    // 특정 tag 를 포함하는 카드 리스트 검색
    List<Card> findByTagContaining(String keyword);

}
