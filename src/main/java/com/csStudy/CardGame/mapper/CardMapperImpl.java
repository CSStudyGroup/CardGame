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
            return CardDto.builder()
                    .id(card.getId())
                    .question(card.getQuestion())
                    .answer(card.getAnswer())
                    .tags(card.getTags())
                    .cid(card.getCategory().getId())
                    .cname(card.getCategory().getName())
                    .build();
        }
    }
}
