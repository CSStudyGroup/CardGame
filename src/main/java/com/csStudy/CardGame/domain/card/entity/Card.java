package com.csStudy.CardGame.domain.card.entity;


import com.csStudy.CardGame.domain.category.entity.Category;
import lombok.*;

import javax.persistence.*;

@Entity
@Cacheable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    @SequenceGenerator(name = "card_seq", sequenceName = "card_sequence")
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;

    public void changeQuestion(String question) {
        this.question = question;
    }

    public void changeAnswer(String answer) {
        this.answer = answer;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

}