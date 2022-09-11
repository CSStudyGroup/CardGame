package com.csStudy.CardGame.domain.card.mapper;

import com.csStudy.CardGame.domain.card.dto.NewCardForm;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.card.dto.CardDto;


public interface CardMapper {
    Card toEntity(NewCardForm newCardForm);

    CardDto toDetailCard(Card card);

}
