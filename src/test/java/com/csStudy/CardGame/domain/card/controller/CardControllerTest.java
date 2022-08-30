package com.csStudy.CardGame.domain.card.controller;

import com.csStudy.CardGame.domain.card.dto.DetailCard;
import com.csStudy.CardGame.domain.card.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;


import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class, SpringExtension.class})
@WebMvcTest(controllers = {CardController.class},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, WebSecurityConfigurerAdapter.class, OncePerRequestFilter.class})})
@AutoConfigureRestDocs
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Nested
    @DisplayName("조건에 맞는 카드 리스트를 조회")
    class WhenGetCards {
        // 테스트용 전체 DetailCard 리스트
        List<DetailCard> cardsInDB = List.of(
                DetailCard.builder()
                        .id(1L)
                        .question("question 1")
                        .answer("answer 1")
                        .tags("tag1")
                        .cid(1L)
                        .cname("category 1")
                        .build(),
                DetailCard.builder()
                        .id(2L)
                        .question("question 2")
                        .answer("answer 2")
                        .tags("tag2")
                        .cid(1L)
                        .cname("category 1")
                        .build(),
                DetailCard.builder()
                        .id(3L)
                        .question("question 3")
                        .answer("answer 3")
                        .tags("tag 3")
                        .cid(2L)
                        .cname("category 2")
                        .build(),
                DetailCard.builder()
                        .id(4L)
                        .question("question 4")
                        .answer("answer 4")
                        .tags("tag1, tag2")
                        .cid(2L)
                        .cname("category 2")
                        .build(),
                DetailCard.builder()
                        .id(5L)
                        .question("question 5")
                        .answer("answer 5")
                        .tags("tag1, tag3")
                        .cid(3L)
                        .cname("category 3")
                        .build(),
                DetailCard.builder()
                        .id(6L)
                        .question("question 6")
                        .answer("answer 6")
                        .tags("tag2, tag3")
                        .cid(3L)
                        .cname("category 3")
                        .build(),
                DetailCard.builder()
                        .id(7L)
                        .question("question 7")
                        .answer("answer 7")
                        .tags("tag1, tag2, tag3")
                        .cid(4L)
                        .cname("category 4")
                        .build()
        );

        @Test
        @DisplayName("모든 카드를 조회하는 경우")
        @WithMockUser(roles = {"USER"}, username = "david")
        void withNoRequestParam() throws Exception {
            given(cardService.getAllCards()).willReturn(cardsInDB);
            mockMvc.perform(get("/cards"))
                    .andExpect(status().isOk())
                    .andDo(document("getCards",
                            responseFields(
                                    fieldWithPath("[].id").description("card identifier"),
                                    fieldWithPath("[].question").description("card question"),
                                    fieldWithPath("[].answer").description("card answer"),
                                    fieldWithPath("[].tags").description("card tags"),
                                    fieldWithPath("[].cid").description("category identifier"),
                                    fieldWithPath("[].cname").description("category name")
                            )
                    ));
        }

        @Test
        @DisplayName("선택한 카테고리들에 속하는 카드를 조회하는 경우")
        @WithMockUser(roles = {"USER"}, username = "david")
        void withSelectedCategory() throws Exception {
            List<Long> selectedCategoryList = List.of(1L, 2L);

            List<DetailCard> cardsInSelectedCategory = cardsInDB.stream()
                    .filter((detailCard) -> selectedCategoryList.contains(detailCard.getCid()))
                    .collect(Collectors.toList());

            given(cardService.findCardsByCategories(selectedCategoryList)).willReturn(cardsInSelectedCategory);

            mockMvc.perform(get("/cards")
                            .param("category", "1")
                            .param("category", "2"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(new ObjectMapper().writeValueAsString(cardsInSelectedCategory)))
                    .andDo(document("getCards",
                            requestParameters(
                                    parameterWithName("category").description("selected category")
                            ),
                            responseFields(
                                    fieldWithPath("[].id").description("card identifier"),
                                    fieldWithPath("[].question").description("card question"),
                                    fieldWithPath("[].answer").description("card answer"),
                                    fieldWithPath("[].tags").description("card tags"),
                                    fieldWithPath("[].cid").description("category identifier"),
                                    fieldWithPath("[].cname").description("category name")
                            )
                    ));

        }
    }

    @Test
    void addCard() {
    }

    @Test
    void editCard() {
    }

    @Test
    void deleteCard() {
    }
}