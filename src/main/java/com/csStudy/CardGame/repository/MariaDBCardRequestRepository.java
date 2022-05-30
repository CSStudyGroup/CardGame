package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.CardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class MariaDBCardRequestRepository implements CardRequestRepository {

    private final EntityManager em;

    @Autowired
    public MariaDBCardRequestRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<CardRequest> save(CardRequest cardRequest) {
        em.persist(cardRequest);
        return Optional.ofNullable(cardRequest);
    }

    @Override
    public void remove(CardRequest cardRequest) {
        em.remove(cardRequest);
    }

    @Override
    public Optional<CardRequest> findOne(Long id) {
        return Optional.ofNullable(em.find(CardRequest.class,id));
    }

    @Override
    public List<CardRequest> findAll() {
        return em.createQuery("select c from CardRequest c", CardRequest.class)
                .getResultList();
    }

    @Override
    public List<CardRequest> findByRequesterId(Long id) {
        return em.createQuery("select c from CardRequest c where c.requester_id = :requester_id", CardRequest.class)
                .setParameter("requester_id", id)
                .getResultList();
    }
}
