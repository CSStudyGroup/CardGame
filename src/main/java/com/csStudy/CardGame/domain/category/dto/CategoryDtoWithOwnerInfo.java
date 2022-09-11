package com.csStudy.CardGame.domain.category.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CategoryDtoWithOwnerInfo {
    private Long id;

    private String name;

    private int cardCount;

    private UUID ownerId;

    private String ownerNickname;

    private String ownerEmail;
}
