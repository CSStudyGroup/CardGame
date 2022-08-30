package com.csStudy.CardGame.domain.category.dto;

import com.csStudy.CardGame.domain.card.dto.DetailCard;
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
    private List<DetailCard> cards = new ArrayList<>();

    public void addCardDto(DetailCard detailCard) {
        this.cards.add(detailCard);
        this.cardCount += 1;
    }
}
