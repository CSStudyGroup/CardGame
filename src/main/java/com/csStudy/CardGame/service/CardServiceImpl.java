package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.domain.Member;
import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.repository.CardRepository;
import com.csStudy.CardGame.repository.CategoryRepository;
import com.csStudy.CardGame.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final CardMapper cardMapper;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository,
                           CategoryRepository categoryRepository,
                           MemberRepository memberRepository,
                           CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    @Transactional
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CardDto> findCardsByCategories(List<Long> categoryIdList) {
        return cardRepository.findByCategoryIn(categoryIdList).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardDto> findCardsByCategoryName(String keyword) {
        return cardRepository.findByCategoryNameContaining(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CardDto> findCardsByQuestion(String keyword) {
        return cardRepository.findByQuestionContaining(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CardDto> findCardsByTag(String keyword) {
        return cardRepository.findByTagContaining(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addCard(CardDto cardDto) {
        // 추가할 카드의 카테고리(Category)와 저자(Member) 조회
        Category category = categoryRepository.findOne(cardDto.getCid()).orElse(null);
        Member author = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        if (category == null || author == null) {
            return false;
        }

        Card newCard = Card.createCard(cardDto, category, author);
        category.addCard(newCard);
        author.addAcceptedCards(newCard);

        cardRepository.save(newCard);
        return true;
    }

    @Override
    public void editCard(CardDto cardDto) {
        cardRepository.findOne(cardDto.getId())
                .ifPresent((target) -> {
                    target.updateContent(cardDto);
                    if (!target.getCategory().getId().equals(cardDto.getCid())) {
                        categoryRepository.findOne(cardDto.getCid())
                                .ifPresent(target::changeCategory);
                    }
                });
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.findOne(cardId)
                .ifPresent(cardRepository::delete);
    }

}
