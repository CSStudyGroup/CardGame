package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository("mariadb_card")
public class MariaDBCardRepository implements CardRepository {

    private final EntityManager em;

    @Autowired
    public MariaDBCardRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Card insert(Card card) {
        em.persist(card);
        return card;
    }

    @Override
    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(em.find(Card.class, id));
    }

    @Override
    public List<Card> findAll() {
        return em.createQuery("select c from Card c", Card.class)
                .getResultList();
    }

    @Override
    public List<Card> filterByCategory(String category) {
        return em.createQuery("select c from Card c where c.category = :category", Card.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Card> filterByTag(String tag) {
        return em.createQuery("select c from Card c where c.tags like :tag", Card.class)
                .setParameter("tag", String.format("%%%s%%", tag))
                .getResultList();
    }

    @Override
    public int updateById(Card card) {
        try {
            findById(card.getId())
                    .ifPresentOrElse(target -> {
                        target.setCategory(card.getCategory());
                        target.setQuestion(card.getQuestion());
                        target.setAnswer(card.getAnswer());
                        target.setTags(card.getTags());
                    }, () -> {
                        throw new NoSuchElementException();
                    });
            return 1;
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }

    @Override
    public int deleteById(Long id) {
        try {
            findById(id)
                    .ifPresentOrElse(em::remove, () -> {
                        throw new NoSuchElementException();
                    });
            return 1;
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }
}
