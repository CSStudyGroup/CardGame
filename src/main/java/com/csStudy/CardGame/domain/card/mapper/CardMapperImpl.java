package com.csStudy.CardGame.domain.card.mapper;

import com.csStudy.CardGame.domain.card.dto.CardForm;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.card.dto.CardDto;
import org.springframework.stereotype.Component;

@Component
public class CardMapperImpl implements CardMapper{

    @Override
    public Card toEntity(CardForm cardForm) {
        return Card.builder()
                .question(cardForm.getQuestion())
                .answer(cardForm.getAnswer())
                .build();
    }

    @Override
    public CardDto toDetailCard(Card card) {
        if (card == null) {
            return null;
        }
        else {
            return CardDto.builder()
                    .id(card.getId())
                    .question(card.getQuestion())
                    .answer(card.getAnswer())
                    .categoryId(card.getCategory().getId())
                    .build();
        }
    }


}
