package com.csStudy.CardGame.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryIncludeCardDto {
    private Long id;

    private String name;

    private int cardCount = 0;

    private List<CardDto> cardDtoList = new ArrayList<>();

    public void addCardDto(CardDto cardDto) {
        this.cardDtoList.add(cardDto);
        this.cardCount += 1;
    }
}
