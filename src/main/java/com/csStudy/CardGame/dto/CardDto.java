package com.csStudy.CardGame.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;

    private int cid;

    private String question;

    private String answer;

    private String tags;

}