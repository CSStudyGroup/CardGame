package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CardRequestDto;

import java.util.List;
import java.util.Optional;

public interface CardRequestService {
    Optional<CardRequestDto> save(CardRequestDto cardRequestDto);
    Optional<CardRequestDto> findOne(Long id);
    List<CardRequestDto> findAll();
    List<CardRequestDto> findByRequesterId(Long id);
    Optional<CardDto> acceptRequest(Long id);
}
