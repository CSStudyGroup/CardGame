package com.csStudy.CardGame.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;

    private String question;

    private String answer;

    private String tags;

    private String categoryName;
}