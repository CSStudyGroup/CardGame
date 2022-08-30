package com.csStudy.CardGame.domain.card.mapper;

import com.csStudy.CardGame.domain.card.dto.NewCard;
import com.csStudy.CardGame.domain.card.dto.SimpleCard;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.card.dto.DetailCard;


public interface CardMapper {
    Card toEntity(NewCard newCard);

    DetailCard toDetailCard(Card card);

    SimpleCard toSimpleCard(Card card);

}
