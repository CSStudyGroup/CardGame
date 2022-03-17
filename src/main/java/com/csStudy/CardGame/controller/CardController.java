package com.csStudy.CardGame.controller;
import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    @Autowired
    public CardController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @GetMapping("/card/list")
    public String list(Model model) {
        List<CardDto> cardDtoList = cardService.findAllCards().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("cardDtoList", cardDtoList);
        return "list";
    }

    @GetMapping("/card")
    public String card(Model model) {
        List<CardDto> cardDtoList = cardService.findAllCards().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("cardDtoList", cardDtoList);
        return "card";
    }

    @GetMapping("/card/insert")
    public String cardInsert() {
        return "cardinsert";
    }

    @PostMapping("card/insert")
    public String cardInsert(CardDto cardDto) {
        cardService.addCard(cardMapper.toEntity(cardDto));

        return "redirect:/card";
    }
}