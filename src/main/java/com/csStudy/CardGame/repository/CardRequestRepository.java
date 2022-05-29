package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.CardRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRequestRepository {
    Optional<CardRequest> save(CardRequest cardRequest);
    void remove(CardRequest cardRequest);
    Optional<CardRequest> findOne(Long id);
    List<CardRequest> findAll();
    List<CardRequest> findByRequesterId(Long id);
}
