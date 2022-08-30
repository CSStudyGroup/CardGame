package com.csStudy.CardGame.domain.card.dto;

// 인터뷰, 카테고리 상세보기 등의 페이지에 표시할 가장 단순한 DTO

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleCard {

    private Long id;

    private String question;

    private String answer;

    private String tags;

}
