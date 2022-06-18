package com.csStudy.CardGame.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto implements Comparable<CardDto> {
    private Long id;

    private String question;

    private String answer;

    private String tags;

    private Long cid;

    private String cname;

    private String authorName;

    @Override
    public int compareTo(CardDto o) {
        if (this.id > o.getId()) {
            return 1;
        }
        else {
            return -1;
        }
    }
}