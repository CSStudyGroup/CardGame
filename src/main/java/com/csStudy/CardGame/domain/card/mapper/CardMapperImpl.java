package com.csStudy.CardGame.domain.card.mapper;

import com.csStudy.CardGame.domain.card.dto.NewCard;
import com.csStudy.CardGame.domain.card.dto.SimpleCard;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.card.dto.DetailCard;
import com.csStudy.CardGame.domain.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CardMapperImpl implements CardMapper{

    @Override
    public Card toEntity(NewCard newCard) {
        return Card.builder()
                .question(newCard.getQuestion())
                .answer(newCard.getAnswer())
                .tags(newCard.getTags())
                .build();
    }

    @Override
    public DetailCard toDetailCard(Card card) {
        if (card == null) {
            return null;
        }
        else {
            return DetailCard.builder()
                    .id(card.getId())
                    .question(card.getQuestion())
                    .answer(card.getAnswer())
                    .tags(card.getTags())
                    .cid(card.getCategory().getId())
                    .cname(card.getCategory().getName())
                    .build();
        }
    }

    @Override
    public SimpleCard toSimpleCard(Card card) {
        if (card == null) {
            return null;
        }
        else {
            return SimpleCard.builder()
                    .id(card.getId())
                    .question(card.getQuestion())
                    .answer(card.getAnswer())
                    .tags(card.getTags())
                    .build();
        }
    }

}
