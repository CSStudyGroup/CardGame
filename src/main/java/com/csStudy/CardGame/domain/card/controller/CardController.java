package com.csStudy.CardGame.domain.card.controller;

import com.csStudy.CardGame.domain.card.dto.CardDto;
import com.csStudy.CardGame.domain.card.dto.CardForm;
import com.csStudy.CardGame.domain.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> addCard(@RequestBody CardForm cardForm){
        CardDto addedCard = cardService.addCard(cardForm);
        return ResponseEntity.ok().build();
    }

    // 카드 수정
    @PutMapping("/cards")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void editCard(@RequestBody CardDto cardDto){
        cardService.editCard(cardDto);
    }

    // 카드 삭제
    @DeleteMapping("/cards/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

}
