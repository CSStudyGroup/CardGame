package com.csStudy.CardGame.controller;
import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.service.CardGameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.lang.Long.parseLong;

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
        List<CategoryDto> categoryDtoList = cardService.findCategoryAll();
        model.addAttribute("categoryDtoList", categoryDtoList);
        model.addAttribute("role", "admin");
        return "cardmain";
    }

//    // 선택된 카테고리에 맞게 표시
//    @GetMapping("/card/category")
//    public String category(@RequestParam(value="keyword") String keyword, Model model) {
//        // 해당되는 키워드의 카드리스트를 받아와 반환
//        List<CardDto> cardDtoList = cardService.findCardByCategory(keyword).getCardDtoList();
//        model.addAttribute("cardDtoList", cardDtoList);
//
//        // 카테고리 리스트를 받아오는 부분
//        List<CategoryDto> categoryDtoList = cardService.findCategoryAll();
//        model.addAttribute("categoryDtoList", categoryDtoList);
//        model.addAttribute("role", "admin");
//        return "category";
//    }

//    // navi bar interview checkbox submit
//    @GetMapping("/card/interview")
//    public String interview(@RequestParam("keyword") List<Long> keywords, Model model) {
//        List<CardDto> cardDtoList = cardService.findCardByCategoryIn(keywords);
//        model.addAttribute("cardDtoList", cardDtoList);
//
//        // 카테고리 리스트를 받아오는 부분
//        List<CategoryDto> categoryDtoList = cardService.findCategoryAll();
//        model.addAttribute("categoryDtoList", categoryDtoList);
//
//        // 키워드 모델에 추가
//        model.addAttribute("keywords", keywords);
//        model.addAttribute("role", "admin");
//        return "interview";
//    }

    @GetMapping("/card/management")
    public String categoryManagement(Model model) {
        // 카테고리 리스트를 받아오는 부분
        List<CategoryDto> categoryDtoList = cardService.findCategoryAll();
        model.addAttribute("categoryDtoList", categoryDtoList);
        model.addAttribute("role", "admin");
        return "categorymanage";
    }

    @GetMapping("/card/requestmanage")
    public String requestmanagePage(Model model) {
        // 카테고리 리스트를 받아오는 부분
        List<CategoryDto> categoryDtoList = cardService.findCategoryAll();
        model.addAttribute("categoryDtoList", categoryDtoList);
        model.addAttribute("role", "user");
        return "requestmanage";
    }


    // 이하 AJAX용 함수들

    // 카테고리 목록 전체 호출
    @ResponseBody
    @GetMapping("card/categoryList")
    public List<CategoryDto> categoryList() {
        return cardService.findCategoryAll();
    }

    // 카드 리스트 반환
    @ResponseBody
    @GetMapping("/card/list")
    public List<CardDto> list(@RequestParam("criteria") String criteria,
                              @RequestParam("keyword") String keyword) {
        // 빈 배열 선언
        List<CardDto> cardDtoList = Collections.emptyList();

        switch (criteria) {
            case "tag":
                cardDtoList = cardService.findCardByTag(keyword);
                System.out.println("tag");
                break;
            case "question":
                cardDtoList = cardService.findCardByQuestion(keyword);
                System.out.println("question");
                break;
            case "category":
                cardDtoList = cardService.findCardByCategory(keyword).getCardDtoList();
                System.out.println("category");
                break;
        }
        return cardDtoList;
    }

    // 선택된 카테고리에 맞게 표시
    @ResponseBody
    @GetMapping("/card/category/{name}")
    public List<CardDto> category(@PathVariable("name") String name) {
        // 해당되는 키워드의 카드리스트를 받아와 반환
        List<CardDto> cardDtoList = cardService.findCardByCategory(name).getCardDtoList();

        return cardDtoList;
    }

    // navi bar interview checkbox submit
    @ResponseBody
    @GetMapping("/card/interview")
    public List<CardDto> interview(@RequestParam("keyword") List<String> keywords) {
        // 최적화 필요
        List<CardDto> cardDtoList = cardService.findCardByCategoryIn(keywords);

        return cardDtoList;
    }

    // 카드 추가
    @ResponseBody
    @PostMapping("card/cardInsert")
    public CardDto cardInsert(@RequestBody CardDto cardDto) {
        return cardService.addCard(cardDto);
    }

    // 카드 수정
    @ResponseBody
    @PostMapping("card/cardUpdate")
    public int cardUpdate(@RequestBody CardDto cardDto) {
        return cardService.updateCard(cardDto);
    }

    // 카드 삭제
    @ResponseBody
    @PostMapping("card/cardDelete")
    public int cardDelete(@RequestBody CardDto cardDto) {
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

    // 카테고리 변경 체크
    @ResponseBody
    @PostMapping("card/categoryChange")
    public boolean categoryChange(@RequestBody String jsonList) throws JsonProcessingException {
        JSONObject jObject = new JSONObject(jsonList);
        ObjectMapper mapper = new ObjectMapper();

        // 추가
        List<CategoryDto> insertedCategoryList = new ArrayList<>();
        JSONArray insertList = jObject.getJSONArray("insert");
        for (Object o: insertList) {
            CategoryDto dto = mapper.readValue(o.toString(), CategoryDto.class);
            insertedCategoryList.add(dto);
        }

        // 수정
        List<CategoryDto> updatedCategoryList = new ArrayList<>();
        JSONArray updateList = jObject.getJSONArray("update");
        for (Object o: updateList) {
            CategoryDto dto = mapper.readValue(o.toString(), CategoryDto.class);
            updatedCategoryList.add(dto);
        }

        // 삭제
        Set<Long> deletedCategorySet = new HashSet<>();
        JSONArray deleteList = jObject.getJSONArray("delete");
        for (Object o: deleteList) {
            CategoryDto dto = mapper.readValue(o.toString(), CategoryDto.class);
            deletedCategorySet.add(dto.getId());
        }

        if (cardService.changeCategories(insertedCategoryList, updatedCategoryList, deletedCategorySet)) {
            return true;
        }
        else
            return false;
    }
}