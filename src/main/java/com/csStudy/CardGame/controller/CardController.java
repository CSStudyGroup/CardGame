package com.csStudy.CardGame.controller;
import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import com.csStudy.CardGame.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Id;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CardController(CardService cardService, CardMapper cardMapper, CategoryMapper categoryMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.categoryMapper = categoryMapper;
    }

    // 메인
    @GetMapping("/card")
    public String card(Model model) {
        // 카테고리 리스트를 받아오는 부분
        /*
        List<CategoryDto> categoryDtoList = cardService.findAllCategories().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("categoryDtoList", categoryDtoList);
         */
        return "card";
    }

    // 선택된 카테고리에 맞게 표시
    @GetMapping("/card/category")
    public String category(@RequestParam(value="keyword") String keyword, Model model) {
        // 해당되는 키워드의 카드리스트를 받아와 반환
        List<CardDto> cardDtoList = cardService.filterCardsByCategory(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("cardDtoList", cardDtoList);
        return "category";
    }

    // navi bar interview checkbox submit
    @GetMapping("/card/interview")
    public String interview(@RequestParam("keyword") List<String> keywords, Model model) {
        List<CardDto> cardDtoList = Collections.<CardDto>emptyList();

        // 체크박스로 선택된 category마다 요청하여 추가
        for (String keyword : keywords){
            List<CardDto> tempList = cardService.filterCardsByCategory(keyword).stream()
                    .map(cardMapper::toDto)
                    .collect(Collectors.toList());
            cardDtoList.addAll(tempList);
        }

        // 결과 최종 반환
        model.addAttribute("cardDtoList", cardDtoList);
        return "interview";
    }

    @GetMapping("/card/categoryManage")
    public String categoryManage(Model model) {
        // 카테고리 리스트를 받아오는 부분
        /*
        List<CategoryDto> categoryDtoList = cardService.findAllCategories().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("categoryDtoList", categoryDtoList);
         */
        return "categoryManage";
    }

    @GetMapping("/card/list")
    public String list(@RequestParam("tag") String tag,
                       @RequestParam("question") String question,
                       @RequestParam("category") String category, Model model) {
        // 빈 배열 선언
        List<CardDto> cardDtoList = Collections.<CardDto>emptyList();

        if (tag != "") {
            cardDtoList = cardService.filterCardsByTag(tag).stream()
                    .map(cardMapper::toDto)
                    .collect(Collectors.toList());
        }
        else if (question != "") {
            /*
            cardDtoList = cardService.filterCardsByQuestion(question).stream()
                    .map(cardMapper::toDto)
                    .collect(Collectors.toList());
             */
        }
        else if (category != "") {
            cardDtoList = cardService.filterCardsByCategory(category).stream()
                    .map(cardMapper::toDto)
                    .collect(Collectors.toList());
        }

        // 모델 추가
        model.addAttribute("cardDtoList", cardDtoList);

        return "list";
    }


    // 이하 AJAX용 함수들

    // 카드 추가
    @PostMapping("card/cardInsert")
    public void cardInsert(HttpServletResponse response, CardDto cardDto) {
        cardService.addCard(cardMapper.toEntity(cardDto));
    }

    // 카드 수정
    @PostMapping("card/cardUpdate")
    public void cardUpdate(HttpServletResponse response, CardDto cardDto) {
        // 변화시킬 엔티티 전달
        //cardService.updateCard(cardMapper.toEntity(cardDto));
    }

    // 카드 삭제
    // 보안상에 문제가 있다면 DTO 전체를 해야할 가능성 유
    @PostMapping("card/cardDelete/{no}")
    public void cardDelete(HttpServletResponse response, @PathVariable("no") Long no) {
        // 삭제할 카드 id 전달
        //cardService.deleteCard(no);
    }

    // 카테고리 추가
    @PostMapping("card/categoryInsert")
    public void categoryInsert(HttpServletResponse response, CategoryDto categoryDto) {
        //cardService.addCategory(categoryMapper.toEntity(categoryDto));
    }

    // 카테고리 수정
    @PostMapping("card/categoryUpdate")
    public void categoryUpdate(HttpServletResponse response, CategoryDto categoryDto) {
        // 변화시킬 엔티티 전달
        //cardService.updateCategory(categoryMapper.toEntity(categoryDto));
    }

    // 카테고리 삭제
    // 보안상에 문제가 있다면 DTO 전체를 해야할 가능성 유
    @PostMapping("card/categoryDelete/{no}")
    public void categoryDelete(HttpServletResponse response, @PathVariable("no") Long no) {
        // 삭제할 카테고리 id 전달
        //cardService.deleteCategory(no);
    }
}