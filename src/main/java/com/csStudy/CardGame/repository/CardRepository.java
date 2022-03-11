package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Card;

import java.util.List;
import java.util.Optional;


public interface CardRepository {
    // 카드 추가
    Card insert(Card card);

    // id로 카드 검색
    Optional<Card> findById(Long id);

    // 모든 카드
    List<Card> findAll();

    // 특정 category 에 해당하는 카드 리스트 검색
    List<Card> filterByCategory(String category);

    // 특정 tag 를 포함하는 카드 리스트 검색
    List<Card> filterByTag(String tag);

    // 카드 수정
    int updateById(Card card);

    // 카드 삭제
    int deleteById(Long id);
}
