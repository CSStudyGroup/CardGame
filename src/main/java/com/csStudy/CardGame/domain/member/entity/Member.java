package com.csStudy.CardGame.domain.member.entity;

import com.csStudy.CardGame.domain.cardrequest.entity.CardRequest;
import com.csStudy.CardGame.domain.card.entity.Card;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @SequenceGenerator(name = "member_seq", sequenceName = "member_sequence")
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column
    private String password;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "member_role")
    @Column(name = "role")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private Set<Card> acceptedCards = new HashSet<>();

    @OneToMany(mappedBy = "requester")
    @Builder.Default
    private Set<CardRequest> requestedCards = new HashSet<>();

    public void changePassword(String password) {
        this.password = password;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addAcceptedCard(Card card) {
        this.acceptedCards.add(card);
    }

    public void removeAcceptedCard(Card card) {
        this.acceptedCards.remove(card);
    }

    public void addRequestedCard(CardRequest cardRequest) {
        this.requestedCards.add(cardRequest);
    }

    public void removeRequestedCard(CardRequest cardRequest) {
        this.requestedCards.remove(cardRequest);
    }

}
