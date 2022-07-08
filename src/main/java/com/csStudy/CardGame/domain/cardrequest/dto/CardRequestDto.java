package com.csStudy.CardGame.domain.cardrequest.dto;

import com.csStudy.CardGame.domain.cardrequest.entity.CardRequestStatus;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequestDto {
    private Long id;
    private String question;
    private String answer;
    private String tags;
    private CardRequestStatus cardRequestStatus;
    private Date createdAt;
    private Date modifiedAt;
    private Long categoryId;
    private String categoryName;
    private Long requesterId;
    private String requesterName;
}
