package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.dto.CardDto;


public interface CardMapper {

    CardDto toDto(Card card);

}
