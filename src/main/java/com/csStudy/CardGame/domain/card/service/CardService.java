package com.csStudy.CardGame.domain.card.service;

import com.csStudy.CardGame.domain.card.dto.CardDto;
import com.csStudy.CardGame.domain.card.dto.CardForm;

import java.util.Collection;
import java.util.List;

public interface CardService {
    /* 조회 */
    // 모든 카드
    List<CardDto> getAllCards();

    // id로 카드 조회
    CardDto findCardById(Long id);

    // 특정 카테고리들에 속하는 카드
    List<CardDto> findCardsByCategories(Collection<Long> categoryIdList);

    // 질문에 특정 키워드를 포함하는 카드
    List<CardDto> findCardsByQuestion(String keyword);

    // 새로운 카드를 추가
    CardDto addCard(CardForm cardForm);

    // 카드 정보를 수정
    void editCard(CardDto cardDto);

    // 카드를 삭제
    void deleteCard(Long cardId);

    //=============================================================================================

}
