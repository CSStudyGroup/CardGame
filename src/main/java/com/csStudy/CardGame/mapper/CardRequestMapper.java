package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.CardRequest;
import com.csStudy.CardGame.dto.CardRequestDto;

public interface CardRequestMapper {
    CardRequest toEntity(CardRequestDto cardRequestDto);
    CardRequestDto toDto(CardRequest cardRequest);
}
