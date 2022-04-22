package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.dto.ChangeCategoryResultDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import com.csStudy.CardGame.repository.CardRepository;
import com.csStudy.CardGame.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
    @Transactional
    public CardDto addCard(CardDto cardDto) {
        return cardMapper.toDto(cardRepository.insert(cardMapper.toEntity(cardDto)));
    }

    // 카드 여러개 추가
    @Transactional
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
    @Transactional
    public Optional<CardDto> findCard(Long cardId) {
        return cardRepository.findById(cardId)
                .map(cardMapper::toDto);
    }

    // 카드 전부 가져오기
    @Transactional
    public List<CardDto> findAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리로 카드 필터링
    @Transactional
    public List<CardDto> filterCardsByCategory(int cid) {
        return cardRepository.filterByCategory(cid).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 여러개의 카테고리로 카드 필터링
    @Transactional
    public List<CardDto> filterCardsByCategories(List<Integer> cidList) {
        return cardRepository.filterByCategories(cidList).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 질문에 키워드가 포함된 카드 필터링
    @Transactional
    public List<CardDto> filterCardsByQuestion(String keyword) {
        return cardRepository.filterByQuestionContaining(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 태그로 카드 필터링
    @Transactional
    public List<CardDto> filterCardsByTag(String tag) {
        return cardRepository.filterByTag(tag).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카드 수정
    @Transactional
    public int updateCard(CardDto cardDto) {
        return cardRepository.update(cardMapper.toEntity(cardDto));
    }

    // 카드 여러개 수정
    @Transactional
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
    @Transactional
    public int deleteCard(CardDto cardDto) {
        return cardRepository.delete(cardMapper.toEntity(cardDto));
    }

    // 카드 여러개 삭제
    @Transactional
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
    @Cacheable("categoryList")
    @Transactional
    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리 추가
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.insert(categoryMapper.toEntity(categoryDto)));
    }

    // 카테고리 여러개 추가
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
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
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public int updateCategory(CategoryDto categoryDto) {
        return categoryRepository.update(categoryMapper.toEntity(categoryDto));
    }

    // 카테고리 여러개 수정
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
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
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public int deleteCategory(CategoryDto categoryDto) {
        return categoryRepository.delete(categoryMapper.toEntity(categoryDto));
    }

    // 카테고리 여러개 삭제
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
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
    // 카테고리 변경사항 전체반영
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public ChangeCategoryResultDto changeCategories(List<CategoryDto> insertList, List<CategoryDto> updateList, List<CategoryDto> deleteList) {
        ChangeCategoryResultDto changeCategoryResultDto = new ChangeCategoryResultDto();
        changeCategoryResultDto.setDone(false);
        List<Integer> deleteResult = null;
        List<Integer> updateResult = null;
        List<CategoryDto> insertResult = null;

        /* 삭제리스트 처리 */
        if (deleteList != null) {
            try {
                deleteResult = new ArrayList<>();
                for (CategoryDto categoryDto : deleteList) {
                    deleteResult.add(categoryRepository.delete(categoryMapper.toEntity(categoryDto)));
                }
            }
            catch (Exception e) {
                return changeCategoryResultDto;
            }
        }

        /* 수정리스트 처리 */
        if (updateList != null) {
            try {
                // update 실제 처리
                updateResult = new ArrayList<>();
                for (CategoryDto categoryDto : updateList) {
                    updateResult.add(categoryRepository.update(categoryMapper.toEntity(categoryDto)));
                }
            }
            catch (Exception e) {
                return changeCategoryResultDto;
            }
        }

        /* 추가리스트 처리 */
        if (insertList != null) {
            try {
                insertResult = new ArrayList<>();
                for (CategoryDto categoryDto : insertList) {
                    insertResult.add(categoryMapper.toDto(categoryRepository.insert(categoryMapper.toEntity(categoryDto))));
                }
            }
            catch (Exception e) {
                return changeCategoryResultDto;
            }
        }

        /* 모든 작업 정상처리시 결과 세팅 후 반환 */
        changeCategoryResultDto.setDone(true);
        changeCategoryResultDto.setDeleteResult(deleteResult);
        changeCategoryResultDto.setUpdateResult(updateResult);
        changeCategoryResultDto.setInsertResult(insertResult);
        return changeCategoryResultDto;
    }
}
