package com.csStudy.CardGame.domain.card.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailCard implements Comparable<DetailCard> {
    private Long id;

    private String question;

    private String answer;

    private String tags;

    private Long cid;

    private String cname;

    @Override
    public int compareTo(DetailCard o) {
        if (this.id > o.getId()) {
            return 1;
        }
        else {
            return -1;
        }
    }
}