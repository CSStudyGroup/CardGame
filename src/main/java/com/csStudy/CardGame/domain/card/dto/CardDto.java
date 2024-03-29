package com.csStudy.CardGame.domain.card.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;

    private String question;

    private String answer;
}