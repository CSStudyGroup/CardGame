package com.csStudy.CardGame.domain.category.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCategory {

    private Long id;

    private String name;

    private int cardCount;
}