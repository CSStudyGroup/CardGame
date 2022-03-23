package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.dto.CardDto;

public class CardMapperImpl implements CardMapper{

    @Override
    public Card toEntity(CardDto cardDto) {
        if (cardDto == null) {
            return null;
        }
        else {
            Card card = new Card();
            card.setId(cardDto.getId());
            card.setCid(cardDto.getCid());
            card.setQuestion(cardDto.getQuestion());
            card.setAnswer(cardDto.getAnswer());
            card.setTags(cardDto.getTags());
            return card;
        }
    }

    @Override
    public CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }
        else {
            CardDto cardDto = new CardDto();
            cardDto.setId(card.getId());
            cardDto.setCid(card.getCid());
            cardDto.setQuestion(card.getQuestion());
            cardDto.setAnswer(card.getAnswer());
            cardDto.setTags(card.getTags());
            return cardDto;
        }
    }
}
