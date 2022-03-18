package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.repository.CardRepository;
import com.csStudy.CardGame.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CardService(@Qualifier("mariadb_card") CardRepository cardRepository, @Qualifier("mariadb_category")CategoryRepository categoryRepository) {
        this.cardRepository = cardRepository;
        this.categoryRepository = categoryRepository;
    }

    // 카드 추가
    public void addCard(Card card) {
        cardRepository.insert(card);
    }

    // id로 카드 찾기
    public Optional<Card> findCard(Long cardId) {
        return cardRepository.findById(cardId);
    }

    // 카드 전부 가져오기
    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    // 카테고리로 카드 필터링
    public List<Card> filterCardsByCategory(String category) {
        return cardRepository.filterByCategory(category);
    }

    // 태그로 카드 필터링
    public List<Card> filterCardsByTag(String tag) {
        return cardRepository.filterByTag(tag);
    }

}
