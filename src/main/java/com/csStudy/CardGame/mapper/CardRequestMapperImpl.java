package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.CardRequest;
import com.csStudy.CardGame.dto.CardRequestDto;

public class CardRequestMapperImpl implements CardRequestMapper {

    @Override
    public CardRequest toEntity(CardRequestDto cardRequestDto) {
        return CardRequest.builder()
                .id(cardRequestDto.getId())
                .question(cardRequestDto.getQuestion())
                .answer(cardRequestDto.getAnswer())
                .tags(cardRequestDto.getTags())
                .requestStatus(cardRequestDto.getRequestStatus())
                .build();
    }

    @Override
    public CardRequestDto toDto(CardRequest cardRequest) {
        return CardRequestDto.builder()
                .id(cardRequest.getId())
                .question(cardRequest.getQuestion())
                .answer(cardRequest.getAnswer())
                .tags(cardRequest.getTags())
                .requestStatus(cardRequest.getRequestStatus())
                .categoryId(cardRequest.getCategory().getId())
                .categoryName(cardRequest.getCategory().getName())
                .requesterId(cardRequest.getRequester().getId())
                .requesterName(cardRequest.getRequester().getNickname())
                .createdAt(cardRequest.getCreatedAt())
                .modifiedAt(cardRequest.getModifiedAt())
                .build();
    }
}
