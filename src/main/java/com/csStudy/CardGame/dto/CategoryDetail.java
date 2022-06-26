package com.csStudy.CardGame.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class CategoryDetail {
    private Long id;

    private String name;

    private int cardCount = 0;

    private List<CardDto> cards = new ArrayList<>();

    public void addCardDto(CardDto cardDto) {
        this.cards.add(cardDto);
        this.cardCount += 1;
    }
}
