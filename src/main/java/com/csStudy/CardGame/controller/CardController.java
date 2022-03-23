package com.csStudy.CardGame.controller;
import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.service.CardGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class CardController {

    private final CardGameService cardService;

    @Autowired
    public CardController(CardGameService cardService) {
        this.cardService = cardService;
    }

    // 메인
    @GetMapping("/card")
    public String card(Model model) {
        // 카테고리 리스트를 받아오는 부분
        List<CategoryDto> categoryDtoList = cardService.findAllCategories();
        model.addAttribute("categoryDtoList", categoryDtoList);
        return "card";
    }

    // 선택된 카테고리에 맞게 표시
    @GetMapping("/card/category")
    public String category(@RequestParam(value="keyword") int keyword, Model model) {
        // 해당되는 키워드의 카드리스트를 받아와 반환
        List<CardDto> cardDtoList = cardService.filterCardsByCategory(keyword);
        model.addAttribute("cardDtoList", cardDtoList);

        // 카테고리 리스트를 받아오는 부분
        List<CategoryDto> categoryDtoList = cardService.findAllCategories();
        model.addAttribute("categoryDtoList", categoryDtoList);
        return "category";
    }

    // navi bar interview checkbox submit
    @GetMapping("/card/interview")
    public String interview(@RequestParam("keyword") List<Integer> keywords, Model model) {
        List<CardDto> cardDtoList = cardService.filterCardsByCategories(keywords);
        model.addAttribute("cardDtoList", cardDtoList);

        // 카테고리 리스트를 받아오는 부분
        List<CategoryDto> categoryDtoList = cardService.findAllCategories();
        model.addAttribute("categoryDtoList", categoryDtoList);
        return "interview";
    }

    @GetMapping("/card/management")
    public String categoryManagement(Model model) {
        // 카테고리 리스트를 받아오는 부분
        List<CategoryDto> categoryDtoList = cardService.findAllCategories();
        model.addAttribute("categoryDtoList", categoryDtoList);
        return "categorymanage";
    }

    @GetMapping("/card/list")
    public String list(@RequestParam("tag") String tag,
                       @RequestParam("question") String question,
                       @RequestParam("cid") Integer cid, Model model) {
        // 빈 배열 선언
        List<CardDto> cardDtoList = Collections.<CardDto>emptyList();

        if (tag != "") {
            cardDtoList = cardService.filterCardsByTag(tag);
        }
        else if (question != "") {
            cardDtoList = cardService.filterCardsByQuestion(question);
        }
        else if (cid != null) {
            cardDtoList = cardService.filterCardsByCategory(cid);
        }

        // 모델 추가
        model.addAttribute("cardDtoList", cardDtoList);

        // 카테고리 리스트를 받아오는 부분
        List<CategoryDto> categoryDtoList = cardService.findAllCategories();
        model.addAttribute("categoryDtoList", categoryDtoList);

        return "list";
    }


    // 이하 AJAX용 함수들

    // 카테고리 목록 전체 호출
    @ResponseBody
    @PostMapping("card/categoryList")
    public List<CategoryDto> categoryList() {
        List<CategoryDto> categoryDtoList = cardService.findAllCategories();
        return categoryDtoList;
    }

    // 카드 추가
    @ResponseBody
    @PostMapping("card/cardInsert")
    public CardDto cardInsert(CardDto cardDto) {
        return cardService.addCard(cardDto);
    }

    // 카드 수정
    @ResponseBody
    @PostMapping("card/cardUpdate")
    public int cardUpdate(CardDto cardDto) {
        return cardService.updateCard(cardDto);
    }

    // 카드 삭제
    @ResponseBody
    @PostMapping("card/cardDelete")
    public int cardDelete(CardDto cardDto) {
        return cardService.deleteCard(cardDto);
    }

    // 카테고리 추가
    @ResponseBody
    @PostMapping("card/categoryInsert")
    public CategoryDto categoryInsert(CategoryDto categoryDto) {
        return cardService.addCategory(categoryDto);
    }

    // 카테고리 수정
    @ResponseBody
    @PostMapping("card/categoryUpdate")
    public int categoryUpdate(CategoryDto categoryDto) {
        return cardService.updateCategory(categoryDto);
    }

    // 카테고리 삭제
    @ResponseBody
    @PostMapping("card/categoryDelete")
    public int categoryDelete(CategoryDto categoryDto) {
        return cardService.deleteCategory(categoryDto);
    }
}