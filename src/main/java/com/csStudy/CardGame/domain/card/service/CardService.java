package com.csStudy.CardGame.domain.card.service;

import com.csStudy.CardGame.domain.card.dto.DetailCard;
import com.csStudy.CardGame.domain.card.dto.NewCard;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CardService {
    /* 조회 */
    // 모든 카드
    List<DetailCard> getAllCards();

    // id로 카드 조회
    Optional<DetailCard> findCardById(Long id);

    // 특정 카테고리들에 속하는 카드
    List<DetailCard> findCardsByCategories(Collection<Long> categoryIdList);

    // 카테고리 이름에 특정 키워드를 포함하는 카드
    List<DetailCard> findCardsByCategoryName(String keyword);

    // 질문에 특정 키워드를 포함하는 카드
    List<DetailCard> findCardsByQuestion(String keyword);

    // 태그에 특정 키워드를 포함하는 카드
    List<DetailCard> findCardsByTags(String keyword);

    // 새로운 카드를 추가
    DetailCard addCard(NewCard newCard);

    // 카드 정보를 수정
    void editCard(DetailCard detailCard);

    // 카드를 삭제
    void deleteCard(Long cardId);

}
