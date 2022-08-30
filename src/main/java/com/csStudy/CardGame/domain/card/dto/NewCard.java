package com.csStudy.CardGame.domain.card.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCard {

    private String question;

    private String answer;

    private String tags;

    private Long categoryId;
}
