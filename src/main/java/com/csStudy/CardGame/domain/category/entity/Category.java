package com.csStudy.CardGame.domain.category.entity;


import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // TODO: 2022-09-10 조회성 API 호출시에만 가져오도록 해야함
    @Formula("(select count(*) from card c where c.category_id = id)")
    @Builder.Default
    private Integer cardCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_id"
    )
    private Member owner;

    public void changeName(String name) {
        this.name = name;
    }
}