package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository("mariadb")
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
        findById(card.getId())
                .ifPresent(target -> {
                    target.setCategory(card.getCategory());
                    target.setQuestion(card.getQuestion());
                    target.setAnswer(card.getAnswer());
                    target.setTags(card.getTags());
                });
        return 1;
    }

    @Override
    public int deleteById(Long id) {
        findById(id)
                .ifPresent(em::remove);
        return 1;
    }
}
