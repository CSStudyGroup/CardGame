package com.csStudy.CardGame.domain.category.dto;

import com.csStudy.CardGame.domain.card.dto.CardDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class CategoryDetail {
    private Long id;

    private String name;

    @Builder.Default
    private int cardCount = 0;

    @Builder.Default
    private List<CardDto> cards = new ArrayList<>();

    public void addCardDto(CardDto cardDto) {
        this.cards.add(cardDto);
        this.cardCount += 1;
    }
}
