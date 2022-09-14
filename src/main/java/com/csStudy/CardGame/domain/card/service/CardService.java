package com.csStudy.CardGame.domain.card.service;

import com.csStudy.CardGame.domain.card.dto.CardDto;
import com.csStudy.CardGame.domain.card.dto.EditCardForm;
import com.csStudy.CardGame.domain.card.dto.NewCardForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardService {

    // 새로운 카드를 추가
    CardDto addCard(NewCardForm newCardForm);

    // 카드 정보를 수정
    void editCard(EditCardForm editCardForm);

    CardDto getCard(Long cardId);

    List<CardDto> getCardsByCategory(Long categoryId, Pageable pageable);

    List<CardDto> getCardsBySearchKeyword(String keyword, Pageable pageable);

    // 카드를 삭제
    void deleteCard(Long cardId);

}
