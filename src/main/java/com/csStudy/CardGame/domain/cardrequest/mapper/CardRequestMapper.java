package com.csStudy.CardGame.domain.cardrequest.mapper;

import com.csStudy.CardGame.domain.cardrequest.entity.CardRequest;
import com.csStudy.CardGame.domain.cardrequest.dto.CardRequestDto;

public interface CardRequestMapper {
    CardRequest toEntity(CardRequestDto cardRequestDto);
    CardRequestDto toDto(CardRequest cardRequest);
}
