package com.csStudy.CardGame.domain.category.dto;

import com.csStudy.CardGame.domain.card.dto.CardDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DetailCategory {
    private Long id;

    private String name;

    private int cardCount;

    private Long ownerId;

    private String ownerName;

    private List<CardDto> cards;
}
