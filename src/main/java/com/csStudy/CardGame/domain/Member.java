package com.csStudy.CardGame.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(nullable = true)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "member_role")
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Card> acceptedCards = new HashSet<>();

    @OneToMany(mappedBy = "requester")
    private Set<CardRequest> requestedCards = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addAcceptedCards(Card card) {
        this.acceptedCards.add(card);
    }

    public void addRequestedCards(CardRequest cardRequest) {
        this.requestedCards.add(cardRequest);
    }

}
