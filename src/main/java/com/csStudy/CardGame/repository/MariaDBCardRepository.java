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
    public int insert(Card card) {
        try {
            em.persist(card);
            return 1;
        }
        catch(Exception e) {
            return 0;
        }
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
    public List<Card> filterByCategories(List<String> categories) {
        return em.createQuery("select c from Card c where c.category in (:categories)", Card.class)
                .setParameter("categories", categories)
                .getResultList();
    }

    @Override
    public List<Card> filterByQuestionContaining(String keyword) {
        return em.createQuery("select c from Card c where c.question like :keyword", Card.class)
                .setParameter("keyword", String.format("%%%s%%", keyword))
                .getResultList();
    }

    @Override
    public List<Card> filterByTag(String tag) {
        return em.createQuery("select c from Card c where c.tags like :tag", Card.class)
                .setParameter("tag", String.format("%%%s%%", tag))
                .getResultList();
    }

    @Override
    public int update(Card card) {
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
    public int delete(Card card) {
        try {
            findById(card.getId())
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
