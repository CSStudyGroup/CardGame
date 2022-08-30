package com.csStudy.CardGame.domain.category.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCategory {
    private String name;

    private int cardCount;
}
