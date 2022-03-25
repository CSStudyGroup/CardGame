package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import com.csStudy.CardGame.repository.CardRepository;
import com.csStudy.CardGame.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardGameService {
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final CardMapper cardMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CardGameService(@Qualifier("mariadb_card") CardRepository cardRepository,
                           @Qualifier("mariadb_category")CategoryRepository categoryRepository,
                           CardMapper cardMapper,
                           CategoryMapper categoryMapper) {
        this.cardRepository = cardRepository;
        this.categoryRepository = categoryRepository;
        this.cardMapper = cardMapper;
        this.categoryMapper = categoryMapper;
    }

    // 카드 추가
    public CardDto addCard(CardDto cardDto) {
        return cardMapper.toDto(cardRepository.insert(cardMapper.toEntity(cardDto)));
    }

    // 카드 여러개 추가
    public List<CardDto> addCards(List<CardDto> cardDtoList) {
        if (cardDtoList == null) {
            return null;
        }
        List<CardDto> result = new ArrayList<>();
        for(CardDto cardDto:cardDtoList) {
            result.add(cardMapper.toDto(cardRepository.insert(cardMapper.toEntity(cardDto))));
        }
        return result;
    }

    // id로 카드 찾기
    public Optional<CardDto> findCard(Long cardId) {
        return cardRepository.findById(cardId)
                .map(cardMapper::toDto);
    }

    // 카드 전부 가져오기
    public List<CardDto> findAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리로 카드 필터링
    public List<CardDto> filterCardsByCategory(int cid) {
        return cardRepository.filterByCategory(cid).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 여러개의 카테고리로 카드 필터링
    public List<CardDto> filterCardsByCategories(List<Integer> cidList) {
        return cardRepository.filterByCategories(cidList).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 질문에 키워드가 포함된 카드 필터링
    public List<CardDto> filterCardsByQuestion(String keyword) {
        return cardRepository.filterByQuestionContaining(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 태그로 카드 필터링
    public List<CardDto> filterCardsByTag(String tag) {
        return cardRepository.filterByTag(tag).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카드 수정
    public int updateCard(CardDto cardDto) {
        return cardRepository.update(cardMapper.toEntity(cardDto));
    }

    // 카드 여러개 수정
    public List<Integer> updateCards(List<CardDto> cardDtoList) {
        if (cardDtoList == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        for(CardDto cardDto:cardDtoList) {
            result.add(cardRepository.update(cardMapper.toEntity(cardDto)));
        }
        return result;
    }

    // 카드 삭제
    public int deleteCard(CardDto cardDto) {
        return cardRepository.delete(cardMapper.toEntity(cardDto));
    }

    // 카드 여러개 삭제
    public List<Integer> deleteCards(List<CardDto> cardDtoList) {
        if (cardDtoList == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        for(CardDto cardDto:cardDtoList) {
            result.add(cardRepository.delete(cardMapper.toEntity(cardDto)));
        }
        return result;
    }

    // 카테고리 전부 가져오기
    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리 추가
    public CategoryDto addCategory(CategoryDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.insert(categoryMapper.toEntity(categoryDto)));
    }

    // 카테고리 여러개 추가
    public List<CategoryDto> addCategories(List<CategoryDto> categoryDtoList) {
        if (categoryDtoList == null) {
            return null;
        }
        List<CategoryDto> result = new ArrayList<>();
        for(CategoryDto categoryDto:categoryDtoList) {
            result.add(categoryMapper.toDto(categoryRepository.insert(categoryMapper.toEntity(categoryDto))));
        }
        return result;
    }

    // 카테고리 수정
    public int updateCategory(CategoryDto categoryDto) {
        return categoryRepository.update(categoryMapper.toEntity(categoryDto));
    }

    // 카테고리 여러개 수정
    public List<Integer> updateCategories(List<CategoryDto> categoryDtoList) {
        if (categoryDtoList == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        for(CategoryDto categoryDto:categoryDtoList) {
            result.add(categoryRepository.update(categoryMapper.toEntity(categoryDto)));
        }
        return result;
    }

    // 카테고리 삭제
    public int deleteCategory(CategoryDto categoryDto) {
        return categoryRepository.delete(categoryMapper.toEntity(categoryDto));
    }

    // 카테고리 여러개 삭제
    public List<Integer> deleteCategories(List<CategoryDto> categoryDtoList) {
        if (categoryDtoList == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        for(CategoryDto categoryDto:categoryDtoList) {
            result.add(categoryRepository.delete(categoryMapper.toEntity(categoryDto)));
        }
        return result;
    }
}
