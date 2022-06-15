package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.CardDto;

import java.util.List;

public interface CardService {
    /* 조회 */
    // 모든 카드
    List<CardDto> getAllCards();

    // 특정 카테고리에 속하는 카드
    List<CardDto> filterCardsByCategory(List<Long> categoryIdList);

    // 질문에 특정 키워드를 포함하는 카드
    List<CardDto> filterCardsByQuestion(String keyword);

    // 태그에 특정 키워드를 포함하는 카드
    List<CardDto> filterCardsByTag(String keyword);
}
