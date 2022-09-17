package com.csStudy.CardGame.domain.bookmark.entity;

import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "bookmark",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "member_category",
                        columnNames = {"member_id", "category_id"}
                )
        }
)
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmark_seq")
    @SequenceGenerator(name = "bookmark_seq", sequenceName = "bookmark_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member member;

    @ManyToOne
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;
}
