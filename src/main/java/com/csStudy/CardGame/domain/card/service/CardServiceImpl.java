package com.csStudy.CardGame.domain.card.service;

import com.csStudy.CardGame.domain.card.dto.NewCardForm;
import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.card.dto.CardDto;
import com.csStudy.CardGame.domain.card.mapper.CardMapper;
import com.csStudy.CardGame.domain.card.repository.CardRepository;
import com.csStudy.CardGame.domain.category.repository.CategoryRepository;
import com.csStudy.CardGame.exception.ApiErrorEnums;
import com.csStudy.CardGame.exception.ApiErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    public CardDto findCardById(Long id) {
        return cardMapper.toDetailCard(cardRepository.findById(id).orElseThrow(() ->
                ApiErrorException.createException(ApiErrorEnums.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, null, null)
        ));
    }

    @Override
    @Transactional
    public List<CardDto> findCardsByCategory(Long categoryId, Pageable pageable) {
        return cardRepository.findByCategory_IdOrderByIdAsc(categoryId, pageable).stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CardDto> findCardsByQuestion(String keyword) {
        return cardRepository.findByQuestionContaining(keyword).stream()
                .map(cardMapper::toDetailCard)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CardDto addCard(NewCardForm newCardForm) {
        // 추가할 카드의 카테고리(Category) 조회
        Category category = categoryRepository.findById(newCardForm.getCategoryId()).orElseThrow(() ->
                ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 카테고리입니다.",
                        null)
        );
        Card insertedCard = cardMapper.toEntity(newCardForm);
        insertedCard.changeCategory(category);
        return cardMapper.toDetailCard(cardRepository.save(insertedCard));
    }

    @Override
    @Transactional
    public void editCard(CardDto cardDto) {
        cardRepository.findById(cardDto.getId())
                .ifPresentOrElse(
                        (target) -> {
                            target.changeQuestion(cardDto.getQuestion());
                            target.changeAnswer(cardDto.getAnswer());
                            if (!target.getCategory().getId().equals(cardDto.getCategoryId())) {
                                categoryRepository.findById(cardDto.getCategoryId())
                                        .ifPresent(target::changeCategory);
                            }},
                        () -> {
                            throw ApiErrorException.createException(ApiErrorEnums.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, null, null);
                        });
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardRepository.findById(cardId)
                .ifPresentOrElse(
                        cardRepository::delete,
                        () -> {
                            throw ApiErrorException.createException(ApiErrorEnums.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, null, null);
                        });
    }
}
