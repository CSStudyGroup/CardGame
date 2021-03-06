package com.csStudy.CardGame.domain.cardrequest.entity;

import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Cacheable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_request_seq")
    @SequenceGenerator(name = "card_request_seq", sequenceName = "card_request_sequence")
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "tags")
    private String tags;

    @Column(name = "request_status", nullable = false, length = 20)
    private CardRequestStatus cardRequestStatus;

    @CreationTimestamp
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;

    @ManyToOne
    @JoinColumn(
            name = "requester_id",
            nullable = false
    )
    private Member requester;
}
