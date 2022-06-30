package com.csStudy.CardGame.controller;

import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.dto.CategoryDetail;
import com.csStudy.CardGame.service.CardService;
import com.csStudy.CardGame.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cardgame/*")
public class CardGameController {

    private final CategoryService categoryService;
    private final CardService cardService;

    @Autowired
    public CardGameController(CategoryService categoryService,
                              CardService cardService) {
        this.categoryService = categoryService;
        this.cardService = cardService;
    }


    /* 카테고리 관련 API */

    // 전체 카테고리 또는 선택된 카테고리 리스트를 반환하는 API
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(required = false) List<Long> selected) {
        if (selected == null) {
            return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(categoryService.getSelectedCategories(selected), HttpStatus.OK);
        }
    }

    // 특정 카테고리를 반환하는 API
    @GetMapping("/categories/{cid}")
    public CategoryDto getCategory(@PathVariable Long cid) {
        return categoryService.getCategoryById(cid);
    }

    // 전체 카테고리 또는 선택된 카테고리 상세조회 리스트를 반환하는 API
    @GetMapping("/categories/detail")
    public List<CategoryDetail> getCategoriesDetail(@RequestParam List<Long> selected) {
        if (selected == null) {
            return categoryService.getAllCategoriesDetail();
        }
        else {
            return categoryService.getSelectedCategoriesDetail(selected);
        }
    }

    // 특정 카테고리 상세 조회 API
    @GetMapping("/categories/detail/{cid}")
    public CategoryDetail getCategoryDetail(@PathVariable Long cid) {
        return categoryService.getCategoryDetailById(cid);
    }

    // 카테고리 변경사항 반영 API
    @PostMapping("/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean changeCategories(@RequestBody String jsonList) throws JsonProcessingException {
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

        return categoryService.changeCategories(insertedCategoryList, updatedCategoryList, deletedCategorySet);
    }


    /* 카드 관련 API */
    // 전체 카드 또는 조건에 맞는 카드의 리스트를 반환하는 API
    @GetMapping("/cards")
    public List<CardDto> getCards(@RequestParam(required = false) List<Long> selected) {
        if (selected != null) {
            return cardService.findCardsByCategories(selected);
        }
        return cardService.getAllCards();
    }

    // 카드 추가
    @PostMapping("/cards")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addCard(@RequestBody CardDto cardDto){
        cardService.addCard(cardDto);
    }

    // 카드 수정
    @PutMapping("/cards")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void editCard(@RequestBody CardDto cardDto){
        cardService.editCard(cardDto);
    }

    // 카드 삭제
    @DeleteMapping("/cards/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }

}
