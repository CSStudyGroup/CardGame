package com.csStudy.CardGame.domain.card.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardForm {

    private String question;

    private String answer;

    private Long categoryId;
}
