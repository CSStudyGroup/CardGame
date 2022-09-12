package com.csStudy.CardGame.domain.card.mapper;

import com.csStudy.CardGame.domain.card.dto.NewCardForm;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.card.dto.CardDto;


public interface CardMapper {

    CardDto toCardDto(Card card);

}
