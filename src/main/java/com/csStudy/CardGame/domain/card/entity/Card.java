package com.csStudy.CardGame.domain.card.entity;


import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.card.dto.CardDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Cacheable
@Getter
@Builder
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

    @ManyToOne
    @JoinColumn(
            name = "author_id",
            nullable = false
    )
    private Member author;

    @Builder
    private Card(Long id, String question, String answer, String tags, Category category, Member author) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.tags = tags;
        category.addCard(this);
        this.category = category;
        author.addAcceptedCard(this);
        this.author = author;
    }

    // 새 엔티티 생성
    public static Card createCard(CardDto cardDto, @NotNull Category category, @NotNull Member author) {
        return Card.builder()
                .category(category)
                .question(cardDto.getQuestion())
                .answer(cardDto.getAnswer())
                .tags(cardDto.getTags())
                .author(author)
                .build();
    }

    // DTO를 받아 컨텐츠(질문, 답변, 태그) 업데이트
    public void updateContent(CardDto cardDto) {
        String question = cardDto.getQuestion();
        String answer = cardDto.getAnswer();
        String tags = cardDto.getTags();
        if (question != null) {
            this.question = question;
        }
        if (answer != null) {
            this.answer = answer;
        }
        if (tags != null) {
            this.tags = tags;
        }
    }

    // 카테고리 변경
    public void setCategory(Category category) {
        // 기존 카테고리와의 관계 해제
        if (this.category != null)
            this.category.removeCard(this);

        // 새 카테고리 설정
        category.addCard(this);
        this.category = category;
    }

    // 저자 설정
    public void setAuthor(Member member) {
        // 기존 멤버와의 관계 해제
        if (this.author != null)
            this.author.removeAcceptedCard(this);

        // 새 멤버 설정
        member.addAcceptedCard(this);
        this.author = member;
    }
}