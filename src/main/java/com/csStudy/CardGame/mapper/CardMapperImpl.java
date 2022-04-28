package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.dto.CardDto;

public class CardMapperImpl implements CardMapper{

    @Override
    public CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }
        else {
            CardDto cardDto = new CardDto();
            cardDto.setId(card.getId());
            cardDto.setQuestion(card.getQuestion());
            cardDto.setAnswer(card.getAnswer());
            cardDto.setTags(card.getTags());
            cardDto.setCategoryName(card.getCategory().getName());
            return cardDto;
        }
    }
}
