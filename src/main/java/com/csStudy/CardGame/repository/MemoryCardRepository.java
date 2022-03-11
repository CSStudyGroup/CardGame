package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Card;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("memory")
public class MemoryCardRepository implements CardRepository {

    private static Map<Long, Card> storage = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Card insert(Card card) {
        card.setId(++sequence);
        storage.put(card.getId(), card);
        return card;
    }

    @Override
    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Card> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Card> filterByCategory(String category) {
        return storage.values().stream()
                .filter(card -> card.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Card> filterByTag(String tag) {
        return storage.values().stream()
                .filter(card -> card.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    @Override
    public int updateById(Card card) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    public void clearStorage() {
        storage.clear();
    }
}
