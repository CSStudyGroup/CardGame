package com.csStudy.CardGame.domain.card.service;

import com.csStudy.CardGame.domain.card.dto.EditCardForm;
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
import org.springframework.dao.DataIntegrityViolationException;
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

    // TODO: 2022-09-14 삽입, 수정, 삭제 API 관련 수정사항 처리
    @Override
    public CardDto addCard(NewCardForm newCardForm) {
        // 추가할 카드의 카테고리(Category) 조회
        Category category = categoryRepository.findById(newCardForm.getCategoryId()).orElseThrow(() ->
                ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 카테고리입니다.",
                        null)
        );

        try {
            return cardMapper.toCardDto(cardRepository.save(Card.builder()
                    .question(newCardForm.getQuestion())
                    .answer(newCardForm.getAnswer())
                    .category(category)
                    .build()));
        }
        catch (DataIntegrityViolationException ex) {
            throw ApiErrorException.createException(
                    ApiErrorEnums.RESOURCE_CONFLICT,
                    HttpStatus.CONFLICT,
                    null,
                    null
            );
        }
    }

    @Override
    @Transactional
    public void editCard(EditCardForm editCardForm) {
        cardRepository.findById(editCardForm.getId())
                .ifPresentOrElse(
                        (target) -> {
                            target.changeQuestion(editCardForm.getQuestion());
                            target.changeAnswer(editCardForm.getAnswer());
                            if (!target.getCategory().getId().equals(editCardForm.getCategoryId())) {
                                categoryRepository.findById(editCardForm.getCategoryId())
                                        .ifPresent(target::changeCategory);
                            }},
                        () -> {
                            throw ApiErrorException.createException(ApiErrorEnums.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND, null, null);
                        });
    }

    @Override
    public CardDto getCard(Long cardId) {
        return cardMapper.toCardDto(cardRepository
                .findById(cardId)
                .orElseThrow(
                        () -> ApiErrorException.createException(
                                ApiErrorEnums.RESOURCE_NOT_FOUND,
                                HttpStatus.NOT_FOUND,
                                null,
                                null
                        )
                ));
    }

    @Override
    public List<CardDto> getCardsByCategory(Long categoryId, Pageable pageable) {
        return cardRepository
                .findByCategory_IdOrderByIdAsc(categoryId, pageable)
                .stream()
                .map(cardMapper::toCardDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getCardsBySearchKeyword(String keyword, Pageable pageable) {
        return cardRepository
                .findByQuestionOrAnswerContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(cardMapper::toCardDto)
                .collect(Collectors.toList());
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
