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
    public void save(Card card) {
        try {
            em.persist(card);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int delete(Card card) {
        try {
            em.remove(card);
            return 1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteById(Long id) {
        // 예외처리 필요
        try {
            em.createQuery("delete from Card c where c.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            return 1;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Optional<Card> findOne(Long id) {
        return Optional.ofNullable(em.find(Card.class, id));
    }

    @Override
    public List<Card> findAll() {
        return em.createQuery("select c from Card c", Card.class)
                .getResultList();
    }

    @Override
    public List<Card> findByCategoryName(String categoryName) {
        return em.createQuery("select c from Card c where c.category.name = :categoryName", Card.class)
                .setParameter("categoryName", categoryName)
                .getResultList();
    }

    @Override
    public List<Card> findByCategoryNameIn(List<String> categoryNameList) {
        return em.createQuery("select c from Card c where c.category.name in :categoryNameList", Card.class)
                .setParameter("categoryNameList", categoryNameList)
                .getResultList();
    }

    @Override
    public List<Card> findByQuestionContaining(String keyword) {
        return em.createQuery("select c from Card c where c.question like :keyword", Card.class)
                .setParameter("keyword", String.format("%%%s%%", keyword))
                .getResultList();
    }

    @Override
    public List<Card> findByTagContaining(String tag) {
        return em.createQuery("select c from Card c where c.tags like :tag", Card.class)
                .setParameter("tag", String.format("%%%s%%", tag))
                .getResultList();
    }

}
