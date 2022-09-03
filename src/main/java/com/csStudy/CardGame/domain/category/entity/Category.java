package com.csStudy.CardGame.domain.category.entity;


import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_sequence")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cnt", nullable = false)
    @Builder.Default
    private Integer cardCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_id",
            nullable = false
    )
    private Member owner;

    @OneToMany(mappedBy = "category")
    @Builder.Default
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Card> cards = new HashSet<>();

    public void changeName(String name) {
        this.name = name;
    }

    public void changeOwner(Member owner) {
        this.owner = owner;
    }

    public void addCard(Card card) {
        if (!this.cards.contains(card)) {
            this.cards.add(card);
            this.cardCount += 1;
        }
    }

    public void removeCard(Card card) {
        if (this.cards.contains(card)) {
            this.cards.remove(card);
            this.cardCount -= 1;
        }
    }
}