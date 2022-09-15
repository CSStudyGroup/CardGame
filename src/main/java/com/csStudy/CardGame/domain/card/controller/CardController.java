package com.csStudy.CardGame.domain.card.controller;

import com.csStudy.CardGame.domain.card.dto.CardDto;
import com.csStudy.CardGame.domain.card.dto.EditCardForm;
import com.csStudy.CardGame.domain.card.dto.NewCardForm;
import com.csStudy.CardGame.domain.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/* 카드 관련 API */
@RestController
public class CardController {
    private final CardService cardService;

    /* Card의 경우  */
    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // 카드 추가
    @PostMapping("/cards")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> addCard(@RequestBody NewCardForm newCardForm){
        CardDto addedCard = cardService.addCard(newCardForm);

        URI location = ServletUriComponentsBuilder
                .fromUriString("/cards")
                .path("/{id}")
                .buildAndExpand(addedCard.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    // 카드 수정
    @PutMapping("/cards")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> editCard(@RequestBody EditCardForm editCardForm){
        cardService.editCard(editCardForm);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long cardId) {
        return ResponseEntity
                .ok()
                .body(cardService.getCard(cardId));
    }

    // 카드 조회
    @GetMapping("/cards")
    public ResponseEntity<List<CardDto>> getCards(
            @RequestParam("cid") Long categoryId,
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return ResponseEntity
                .ok()
                .body(
                        (keyword == null) ?
                                cardService.getCardsByCategory(categoryId, pageable)
                                : cardService.getCardsByCategoryWithSearchKeyword(categoryId, keyword, pageable)
                );
    }

    // 카드 삭제
    @DeleteMapping("/cards/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity
                .ok()
                .build();
    }
}
