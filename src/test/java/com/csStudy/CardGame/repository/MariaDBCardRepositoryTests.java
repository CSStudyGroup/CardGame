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

    @Autowired @Qualifier("mariadb") CardRepository repository;

    @Test
    public void insert() {
        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        com.csStudy.CardGame.domain.Card result = repository.findById(card.getId()).get();
        Assertions.assertThat(result).isEqualTo(card);
    }

    @Test
    public void filterByTag() {
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
        ArrayList<Card> expected = new ArrayList<>();
        expected.add(card);
        expected.add(card2);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void findAll() {
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
        ArrayList<Card> expected = new ArrayList<>();
        expected.add(card);
        expected.add(card2);
        expected.add(card3);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void filterByCategory() {
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
        ArrayList<Card> expected = new ArrayList<>();
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
        card2.setCategory("graph");
        card2.setQuestion("about dynamic Programming2");
        card2.setAnswer("great Answer");
        card2.setTags("test");

        repository.updateById(card);
        List<Card> result = repository.filterByTag("test");
        ArrayList<Card> expected = new ArrayList<>();
        expected.add(card);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void deleteById() {
        Card card = new Card();
        card.setCategory("graph");
        card.setQuestion("about dynamic Programming");
        card.setAnswer("great Answer");
        card.setTags("graph, dijkstra");
        repository.insert(card);

        repository.deleteById(card.getId());
        List<Card> result = repository.findAll();
        ArrayList<Card> expected = new ArrayList<>();

        Assertions.assertThat(result).isEqualTo(expected);
    }
}
