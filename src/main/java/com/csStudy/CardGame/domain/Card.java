package com.csStudy.CardGame.domain;


import com.csStudy.CardGame.dto.CardDto;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "tags")
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "cid",
            nullable = false
    )
    private Category category;

    public static Card createCard(CardDto cardDto) {
        Card newCard = new Card();
        newCard.setQuestion(cardDto.getQuestion());
        newCard.setAnswer(cardDto.getAnswer());
        newCard.setTags(cardDto.getTags());
        return newCard;
    }

}