package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Card;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Transactional
public class MariaDBCardRepositoryTests {

    @Autowired @Qualifier("mariadb_card") CardRepository repository;

    @Test
    public void insert() {
        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        Card result = repository.findById(card.getId()).get();
        Assertions.assertThat(result).isEqualTo(card);
    }

    @Test
    public void filterByTag() {
        List<Card> expected = repository.filterByTag("dijkstra");

        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        Card card2 = new Card();
        card2.setCategory("graph");
        card2.setQuestion("about dynamic Programming2");
        card2.setAnswer("great Answer");
        card2.setTags("graph, dijkstra");
        repository.insert(card2);

        Card card3 = new Card();
        card3.setCategory("graph");
        card3.setQuestion("about dynamic Programming3");
        card3.setAnswer("great Answer");
        card3.setTags("graph");
        repository.insert(card3);

        List<Card> result = repository.filterByTag("dijkstra");
        expected.add(card);
        expected.add(card2);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void findAll() {
        List<Card> expected = repository.findAll();

        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        Card card2 = new Card();
        card2.setCategory("graph");
        card2.setQuestion("about dynamic Programming2");
        card2.setAnswer("great Answer");
        card2.setTags("graph, dijkstra");
        repository.insert(card2);

        Card card3 = new Card();
        card3.setCategory("graph");
        card3.setQuestion("about dynamic Programming3");
        card3.setAnswer("great Answer");
        card3.setTags("graph");
        repository.insert(card3);

        List<Card> result = repository.findAll();
        expected.add(card);
        expected.add(card2);
        expected.add(card3);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void filterByCategory() {
        List<Card> expected = repository.filterByCategory("dynamic_programming");

        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        Card card2 = new Card();
        card2.setCategory("graph");
        card2.setQuestion("about dynamic Programming2");
        card2.setAnswer("great Answer");
        card2.setTags("graph, dijkstra");
        repository.insert(card2);

        Card card3 = new Card();
        card3.setCategory("dynamic_programming");
        card3.setQuestion("about dynamic Programming3");
        card3.setAnswer("great Answer");
        card3.setTags("graph");
        repository.insert(card3);

        List<Card> result = repository.filterByCategory("dynamic_programming");
        expected.add(card3);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void updateById() {
        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        Card card2 = new Card();
        card2.setId(card.getId());
        card2.setCategory("graph");
        card2.setQuestion("about dynamic Programming2");
        card2.setAnswer("great Answer");
        card2.setTags("test");

        repository.update(card2);

        Assertions.assertThat(repository.findById(card.getId()).get().getTags()).isEqualTo("test");
    }

    @Test
    public void deleteById() {
        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        Long targetId = card.getId();

        repository.delete(card);

        Assertions.assertThat(repository.findById(targetId).orElse(null)).isEqualTo(null);
    }

    @Test
    public void filterByCategories() {
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("graph");
        keywords.add("c1");

        List<Card> expected = repository.filterByCategories(keywords);

        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        List<Card> result = repository.filterByCategories(keywords);
        expected.add(card);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void filterByQuestionContaining() {
        String keyword = "est1";

        List<Card> expected = repository.filterByQuestionContaining(keyword);

        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about best1 dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        List<Card> result = repository.filterByQuestionContaining(keyword);
        expected.add(card);

        Assertions.assertThat(result).isEqualTo(expected);
    }
}
