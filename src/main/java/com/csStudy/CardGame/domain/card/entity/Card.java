package com.csStudy.CardGame.domain.card.entity;


import com.csStudy.CardGame.domain.category.entity.Category;
import lombok.*;

import javax.persistence.*;

@Entity
@Cacheable
@Getter
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq")
    @SequenceGenerator(name = "card_seq", sequenceName = "card_sequence")
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "tags")
    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;

    @Builder
    private Card(Long id, String question, String answer, String tags) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tags = tags;
    }

    public void changeQuestion(String question) {
        this.question = question;
    }

    public void changeAnswer(String answer) {
        this.answer = answer;
    }

    public void changeTags(String tags) {
        this.tags = tags;
    }

    public void changeCategory(Category category) {
        // 기존 카테고리와의 관계 해제
        if (this.category != null)
            this.category.removeCard(this);

        // 새 카테고리 설정
        category.addCard(this);
        this.category = category;
    }

}