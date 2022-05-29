package com.csStudy.CardGame.dto;

import com.csStudy.CardGame.domain.RequestStatus;
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
    private RequestStatus requestStatus;
    private Date createdAt;
    private Date modifiedAt;
    private Long categoryId;
    private String categoryName;
    private Long requesterId;
    private String requesterName;
}
