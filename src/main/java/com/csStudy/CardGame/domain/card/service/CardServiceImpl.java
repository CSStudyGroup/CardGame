package com.csStudy.CardGame.domain.card.service;

import com.csStudy.CardGame.domain.card.entity.Card;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.card.dto.CardDto;
import com.csStudy.CardGame.domain.card.mapper.CardMapper;
import com.csStudy.CardGame.domain.card.repository.CardRepository;
import com.csStudy.CardGame.domain.category.repository.CategoryRepository;
import com.csStudy.CardGame.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Optional<CardDto> findCardById(Long id) {
        return cardRepository.findById(id).map(cardMapper::toDto);
    }

    @Override
    @Transactional
    public List<CardDto> findCardsByCategories(Collection<Long> categoryIdList) {
        return cardRepository.findByCategory_IdIn(categoryIdList).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CardDto> findCardsByCategoryName(String keyword) {
        return cardRepository.findByCategory_NameContaining(keyword).stream()
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
    public List<CardDto> findCardsByTags(String keyword) {
        return cardRepository.findByTagsContaining(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CardDto addCard(CardDto cardDto) {
        // 추가할 카드의 카테고리(Category)와 저자(Member) 조회
        Category category = categoryRepository.findById(cardDto.getCid()).orElse(null);
        Member author = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);

        Card newCard = Card.createCard(cardDto, category, author);

        return cardMapper.toDto(cardRepository.save(newCard));
    }

    @Override
    @Transactional
    public void editCard(CardDto cardDto) {
        cardRepository.findById(cardDto.getId())
                .ifPresent((target) -> {
                    target.updateContent(cardDto);
                    if (!target.getCategory().getId().equals(cardDto.getCid())) {
                        categoryRepository.findById(cardDto.getCid())
                                .ifPresent(target::setCategory);
                    }
                    if (!target.getAuthor().getId().equals(cardDto.getAuthorId())) {
                        memberRepository.findById(cardDto.getAuthorId())
                                .ifPresent(target::setAuthor);
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
