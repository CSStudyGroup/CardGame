package com.csStudy.CardGame.domain.card.mapper;

import com.csStudy.CardGame.domain.card.dto.CardForm;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.card.dto.CardDto;


public interface CardMapper {
    Card toEntity(CardForm cardForm);

    CardDto toDetailCard(Card card);

}
