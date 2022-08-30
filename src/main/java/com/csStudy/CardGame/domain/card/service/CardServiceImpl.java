package com.csStudy.CardGame.domain.card.service;

import com.csStudy.CardGame.domain.card.dto.NewCard;
import com.csStudy.CardGame.domain.card.dto.SimpleCard;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.card.dto.DetailCard;
import com.csStudy.CardGame.domain.card.mapper.CardMapper;
import com.csStudy.CardGame.domain.card.repository.CardRepository;
import com.csStudy.CardGame.domain.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final CardMapper cardMapper;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository,
                           CategoryRepository categoryRepository,
                           CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.categoryRepository = categoryRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    @Transactional
    public List<DetailCard> getAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DetailCard> findCardById(Long id) {
        return cardRepository.findById(id).map(cardMapper::toDetailCard);
    }

    @Override
    @Transactional
    public List<DetailCard> findCardsByCategories(Collection<Long> categoryIdList) {
        return cardRepository.findByCategory_IdIn(categoryIdList).stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DetailCard> findCardsByCategoryName(String keyword) {
        return cardRepository.findByCategory_NameContaining(keyword).stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DetailCard> findCardsByQuestion(String keyword) {
        return cardRepository.findByQuestionContaining(keyword).stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DetailCard> findCardsByTags(String keyword) {
        return cardRepository.findByTagsContaining(keyword).stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DetailCard addCard(NewCard newCard) {
        // 추가할 카드의 카테고리(Category)와 저자(Member) 조회
        Category category = categoryRepository.findById(newCard.getCategoryId()).orElse(null);
        Card insertedCard = cardMapper.toEntity(newCard);
        insertedCard.changeCategory(category);

        return cardMapper.toDetailCard(cardRepository.save(insertedCard));
    }

    @Override
    @Transactional
    public void editCard(DetailCard detailCard) {
        cardRepository.findById(detailCard.getId())
                .ifPresent((target) -> {
                    target.changeQuestion(detailCard.getQuestion());
                    target.changeAnswer(detailCard.getAnswer());
                    target.changeTags(detailCard.getTags());
                    if (!target.getCategory().getId().equals(detailCard.getCid())) {
                        categoryRepository.findById(detailCard.getCid())
                                .ifPresent(target::changeCategory);
                    }
                });
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardRepository.findById(cardId)
                .ifPresent(cardRepository::delete);
    }
}
