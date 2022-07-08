package com.csStudy.CardGame.domain.cardrequest.mapper;

import com.csStudy.CardGame.domain.cardrequest.entity.CardRequest;
import com.csStudy.CardGame.domain.cardrequest.dto.CardRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CardRequestMapperImpl implements CardRequestMapper {

    @Override
    public CardRequest toEntity(CardRequestDto cardRequestDto) {
        return CardRequest.builder()
                .id(cardRequestDto.getId())
                .question(cardRequestDto.getQuestion())
                .answer(cardRequestDto.getAnswer())
                .tags(cardRequestDto.getTags())
                .cardRequestStatus(cardRequestDto.getCardRequestStatus())
                .build();
    }

    @Override
    public CardRequestDto toDto(CardRequest cardRequest) {
        return CardRequestDto.builder()
                .id(cardRequest.getId())
                .question(cardRequest.getQuestion())
                .answer(cardRequest.getAnswer())
                .tags(cardRequest.getTags())
                .cardRequestStatus(cardRequest.getCardRequestStatus())
                .categoryId(cardRequest.getCategory().getId())
                .categoryName(cardRequest.getCategory().getName())
                .requesterId(cardRequest.getRequester().getId())
                .requesterName(cardRequest.getRequester().getNickname())
                .createdAt(cardRequest.getCreatedAt())
                .modifiedAt(cardRequest.getModifiedAt())
                .build();
    }
}
