package com.csStudy.CardGame.domain;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cnt", nullable = false)
    private int cardCount;

    @OneToMany(mappedBy = "category")
    private Set<Card> cards = new HashSet<>();

    public static Category createCategory(String name) {
        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setCardCount(0);

        return newCategory;
    }

    public void addCard(Card card) {
        if (!this.cards.contains(card)) {
            this.cards.add(card);
            card.setCategory(this);
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