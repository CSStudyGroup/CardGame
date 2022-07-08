package com.csStudy.CardGame.domain.otherrequest.entity;

import com.csStudy.CardGame.domain.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OtherRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "other_request_seq")
    @SequenceGenerator(name = "other_request_seq", sequenceName = "other_request_sequence")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "request_status", nullable = false, length = 20)
    private OtherRequestStatus requestStatus;

    @CreationTimestamp
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Date modifiedAt;

    @ManyToOne
    @JoinColumn(
            name = "requester_id",
            nullable = false
    )
    private Member requester;

}
