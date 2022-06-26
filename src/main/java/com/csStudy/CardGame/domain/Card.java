package com.csStudy.CardGame.domain;


import com.csStudy.CardGame.dto.CardDto;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Cacheable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public void changeCategory(Category category) {
        // 기존 카테고리와의 관계 해제
        this.category.removeCard(this);

        // 새 카테고리 설정
        category.addCard(this);
        this.category = category;
    }
}