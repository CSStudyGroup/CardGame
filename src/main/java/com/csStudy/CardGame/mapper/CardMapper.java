package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.dto.CardDto;


public interface CardMapper {

    Card toEntity(CardDto cardDto);

    CardDto toDto(Card card);

}
