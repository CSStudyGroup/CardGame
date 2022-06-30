package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.domain.CardRequest;
import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CardRequestDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CardRequestMapper;
import com.csStudy.CardGame.repository.CardRepository;
import com.csStudy.CardGame.repository.CardRequestRepository;
import com.csStudy.CardGame.repository.CategoryRepository;
import com.csStudy.CardGame.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardRequestServiceImpl implements CardRequestService {

    private final CardRequestRepository cardRequestRepository;
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final CardRequestMapper cardRequestMapper;
    private final CardMapper cardMapper;

    @Autowired
    public CardRequestServiceImpl(CardRequestRepository cardRequestRepository,
                                  CardRepository cardRepository,
                                  CategoryRepository categoryRepository,
                                  MemberRepository memberRepository,
                                  CardRequestMapper cardRequestMapper,
                                  CardMapper cardMapper) {
        this.cardRequestRepository = cardRequestRepository;
        this.cardRepository = cardRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.cardRequestMapper = cardRequestMapper;
        this.cardMapper = cardMapper;
    }

    @Override
    @Transactional
    public void save(CardRequestDto cardRequestDto) {
        CardRequest newCardRequest = cardRequestMapper.toEntity(cardRequestDto);
        categoryRepository.findById(cardRequestDto.getCategoryId())
                .ifPresent(newCardRequest::setCategory);
        memberRepository.findById(cardRequestDto.getRequesterId())
                .ifPresent((member) -> {
                    newCardRequest.setRequester(member);
                    member.addRequestedCard(newCardRequest);
                });
        cardRequestRepository.save(newCardRequest);
    }

    @Override
    @Transactional
    public Optional<CardRequestDto> findOne(Long id) {
        return cardRequestRepository.findOne(id)
                .map(cardRequestMapper::toDto);
    }

    @Override
    @Transactional
    public List<CardRequestDto> findAll() {
        return cardRequestRepository.findAll().stream()
                .map(cardRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CardRequestDto> findByRequesterId(Long id) {
        return cardRequestRepository.findByRequesterId(id).stream()
                .map(cardRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void acceptRequest(Long id) {
        CardRequest acceptedRequest = cardRequestRepository.findOne(id).orElse(null);
        if (acceptedRequest != null) {
            Card newCard = Card.builder()
                    .question(acceptedRequest.getQuestion())
                    .answer(acceptedRequest.getAnswer())
                    .tags(acceptedRequest.getTags())
                    .category(acceptedRequest.getCategory())
                    .author(acceptedRequest.getRequester())
                    .build();
            newCard.getCategory().addCard(newCard);
            cardRepository.save(newCard);
        }
    }
}
