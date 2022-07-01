package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.domain.Member;
import com.csStudy.CardGame.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.PersistenceException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
class CardRepositoryTest {

    @Autowired
    private CardRepository testCardRepo;

    @Autowired
    private CategoryRepository testCategoryRepo;

    @Autowired
    private MemberRepository testMemberRepo;

    @Test
    @DisplayName("card save success")
    void saveSuccess() {
        // given
        Category category = Category.builder()
                .name("test category")
                .cardCount(0)
                .build();
        testCategoryRepo.save(category);

        Member member = Member.builder()
                .nickname("test nickname")
                .email("test@email.com")
                .password("1234")
                .roles(Set.of(Role.USER))
                .build();
        testMemberRepo.save(member);

        Card card = Card.builder()
                .question("test question")
                .answer("test answer")
                .tags("test tags")
                .build();
        card.setCategory(category);
        card.setAuthor(member);

        // when
        testCardRepo.save(card);
        Card selected = testCardRepo.findById(card.getId()).orElse(null);

        // then
        assertThat(selected).isEqualTo(card);
    }

    @Test
    @DisplayName("card save failure - not null constraint violation")
    void saveFailureByNotNullConstraint() {
        // given
        Member member = Member.builder()
                .nickname("test nickname")
                .email("test@email.com")
                .password("1234")
                .roles(Set.of(Role.USER))
                .build();
        testMemberRepo.save(member);

        Card card = Card.builder()
                .question("test question")
                .answer("test answer")
                .tags("test tags")
                .build();
        card.setAuthor(member);

        // when, then
        Assertions.assertThrows(PersistenceException.class, () -> {
            testCardRepo.save(card);
        });
    }

    @Test
    @DisplayName("card delete success")
    void deleteSuccess() {

    }















    @Test
    void delete() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findOne() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findByCategoryId() {
    }

    @Test
    void findByCategoryIn() {
    }

    @Test
    void findByCategoryNameContaining() {
    }

    @Test
    void findByQuestionContaining() {
    }

    @Test
    void findByTagContaining() {
    }
}