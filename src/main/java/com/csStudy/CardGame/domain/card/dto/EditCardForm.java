package com.csStudy.CardGame.domain.card.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditCardForm {

    private Long id;

    private String question;

    private String answer;

    private Long categoryId;
}
