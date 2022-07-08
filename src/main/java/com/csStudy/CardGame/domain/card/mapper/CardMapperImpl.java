package com.csStudy.CardGame.domain.card.mapper;

import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.card.dto.CardDto;
import org.springframework.stereotype.Component;

@Component
public class CardMapperImpl implements CardMapper{

    @Override
    public CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }
        else {
            return CardDto.builder()
                    .id(card.getId())
                    .question(card.getQuestion())
                    .answer(card.getAnswer())
                    .tags(card.getTags())
                    .cid(card.getCategory().getId())
                    .cname(card.getCategory().getName())
                    .authorId(card.getAuthor().getId())
                    .authorName(card.getAuthor().getNickname())
                    .build();
        }
    }
}
