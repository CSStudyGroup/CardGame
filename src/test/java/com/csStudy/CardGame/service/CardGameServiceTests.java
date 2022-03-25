package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class CardGameServiceTests {
    @Autowired
    CardGameService cardGameService;
    CardMapper cardMapper;
    CategoryMapper categoryMapper;

    @Test
    public void addCards() {
        CardDto cardDto1 = new CardDto();
        cardDto1.setCid(1);
        cardDto1.setQuestion("about dynamic Programming1");
        cardDto1.setAnswer("great Answer1");
        cardDto1.setTags("graph, dijkstra");

        CardDto cardDto2 = new CardDto();
        cardDto2.setCid(1);
        cardDto2.setQuestion("about dynamic Programming2");
        cardDto2.setAnswer("great Answer2");
        cardDto2.setTags("graph, dijkstra");

        CardDto cardDto3 = new CardDto();
        cardDto3.setCid(1);
        cardDto3.setQuestion("about dynamic Programming3");
        cardDto3.setAnswer("great Answer3");
        cardDto3.setTags("graph, dijkstra");

        int expected = cardGameService.findAllCards().size()+3;

        List<CardDto> cardDtoList = new ArrayList<>();
        cardDtoList.add(cardDto1);
        cardDtoList.add(cardDto2);
        cardDtoList.add(cardDto3);

        cardGameService.addCards(cardDtoList);

        Assertions.assertThat(cardGameService.findAllCards().size()).isEqualTo(expected);
    }

    @Test
    public void updateCards() {
        List<CardDto> cardDtoList = cardGameService.findAllCards();
        for (CardDto cardDto:cardDtoList) {
            cardDto.setTags("test_tag");
        }

        int expected = cardGameService.findAllCards().size();

        cardGameService.updateCards(cardDtoList);

        Assertions.assertThat(cardGameService.filterCardsByTag("test_tag").size()).isEqualTo(expected);
    }

    @Test
    public void deleteCards() {
        CardDto cardDto1 = new CardDto();
        cardDto1.setCid(1);
        cardDto1.setQuestion("about dynamic Programming1");
        cardDto1.setAnswer("great Answer1");
        cardDto1.setTags("graph, dijkstra");

        cardGameService.addCard(cardDto1);

        int expected = 0;

        cardGameService.deleteCards(cardGameService.findAllCards());

        Assertions.assertThat(cardGameService.findAllCards().size()).isEqualTo(expected);
    }

    @Test
    public void addCategories() {
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setCname("c4");

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setCname("c5");

        CategoryDto categoryDto3 = new CategoryDto();
        categoryDto3.setCname("c6");

        int expected = cardGameService.findAllCategories().size()+3;

        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(categoryDto1);
        categoryDtoList.add(categoryDto2);
        categoryDtoList.add(categoryDto3);

        cardGameService.addCategories(categoryDtoList);

        Assertions.assertThat(cardGameService.findAllCategories().size()).isEqualTo(expected);
    }

    @Test
    public void updateCategories() {
        List<CategoryDto> categoryDtoList = cardGameService.findAllCategories();
        int idx = 6;
        int expected = 0;
        for (CategoryDto categoryDto:categoryDtoList) {
            categoryDto.setCname(String.format("c%d", idx));
            expected += idx;
            idx += 1;
        }

        cardGameService.updateCategories(categoryDtoList);

        int result = 0;
        for (CategoryDto categoryDto:cardGameService.findAllCategories()) {
            result += Integer.parseInt(categoryDto.getCname().substring(1));
        }

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteCategories() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCname("c9");

        cardGameService.addCategory(categoryDto);

        int expected = 0;

        cardGameService.deleteCategories(cardGameService.findAllCategories());

        Assertions.assertThat(cardGameService.findAllCategories().size()).isEqualTo(expected);
    }
}
