package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Card;
import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.dto.*;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import com.csStudy.CardGame.repository.CardRepository;
import com.csStudy.CardGame.repository.CategoryRepository;
import com.csStudy.CardGame.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardGameService {
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final CardMapper cardMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CardGameService(@Qualifier("mariadb_card") CardRepository cardRepository,
                           @Qualifier("mariadb_category")CategoryRepository categoryRepository,
                           MemberRepository memberRepository,
                           CardMapper cardMapper,
                           CategoryMapper categoryMapper) {
        this.cardRepository = cardRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.cardMapper = cardMapper;
        this.categoryMapper = categoryMapper;
    }

    // 카드 관련 서비스 메소드

    // 카드 추가
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public CardDto addCard(CardDto cardDto) {
        Card newCard = Card.builder()
                .question(cardDto.getQuestion())
                .answer(cardDto.getAnswer())
                .tags(cardDto.getTags())
                .build();
        // 예외처리 필요
        try {
            categoryRepository.findOne(cardDto.getCid())
                    .ifPresentOrElse((category) -> {
                        newCard.setCategory(category);
                        category.addCard(newCard);
                    }, () -> {
                        throw new NoSuchElementException();
                    });
            memberRepository.findOne(2L)
                    .ifPresentOrElse((author) -> {
                        newCard.setAuthor(author);
                        author.addAcceptedCards(newCard);
                    }, () -> {
                        throw new NoSuchElementException();
                    });
        } catch(Exception e) {
            System.out.println("카드추가시 카테고리, 관리자 탐색 예외처리");
        }
        cardRepository.save(newCard);
        return cardMapper.toDto(newCard);
    }

    // id로 카드 찾기
    @Transactional
    public Optional<CardDto> findOne(Long cardId) {
        return cardRepository.findOne(cardId)
                .map(cardMapper::toDto);
    }

    // 카드 전부 가져오기
    @Transactional
    public List<CardDto> findCardAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리로 카드 필터링
    @Transactional
    public CategoryIncludeCardDto findCardByCategory(long cid) {
        CategoryIncludeCardDto categoryIncludeCardDto = new CategoryIncludeCardDto();
        try {
            Category category = Objects.requireNonNull(categoryRepository.findOne(cid).orElse(null));
            categoryIncludeCardDto.setId(category.getId());
            categoryIncludeCardDto.setName(category.getName());
            categoryIncludeCardDto.setCardCount(category.getCardCount());
            categoryIncludeCardDto.setCardDtoList(category.getCards().stream()
                    .map(cardMapper::toDto).sorted().collect(Collectors.toList()));
        }
        catch (Exception e) {
            return categoryIncludeCardDto;
        }
        return categoryIncludeCardDto;
    }

    // 여러개의 카테고리로 카드 필터링
    @Transactional
    public List<CardDto> findCardByCategoryIn(List<Long> cidList) {
        return cardRepository.findByCategoryIn(cidList).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 질문에 키워드가 포함된 카드 필터링
    @Transactional
    public List<CardDto> findCardByQuestion(String keyword) {
        return cardRepository.findByQuestionContaining(keyword).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 태그로 카드 필터링
    @Transactional
    public List<CardDto> findCardByTag(String tag) {
        return cardRepository.findByTagContaining(tag).stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카드 수정
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public int updateCard(CardDto cardDto) {
        // 비즈니스 로직으로 구현
        // 예외처리 필요
        Card target = Objects.requireNonNull(cardRepository.findOne(cardDto.getId()).orElse(null));
        Category changedCategory = Objects.requireNonNull(categoryRepository.findOne(cardDto.getCid()).orElse(null));
        target.getCategory().removeCard(target);
        changedCategory.addCard(target);

        target.setQuestion(cardDto.getQuestion());
        target.setAnswer(cardDto.getAnswer());
        target.setTags(cardDto.getTags());

        return 1;
    }

    // 카드 삭제
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public int deleteCard(CardDto cardDto) {
        // 예외처리 필요
        System.out.println("============================================================================");
        Card target = Objects.requireNonNull(cardRepository.findOne(cardDto.getId()).orElse(null));
        Category changedCategory = Objects.requireNonNull(categoryRepository.findOne(cardDto.getCid()).orElse(null));
        changedCategory.removeCard(target);
        System.out.println("============================================================================");
        return cardRepository.deleteById(cardDto.getId());
    }


    // 카테고리 관련 서비스 메소드
    // 카테고리 전부 가져오기
    @Cacheable("categoryList")
    @Transactional
    public List<CategoryDto> findCategoryAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리 추가
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        // 예외처리 필요
        Category newCategory = Category.createCategory(categoryDto.getName());
        categoryRepository.save(newCategory);
        return categoryMapper.toDto(newCategory);
    }

    // 카테고리 수정
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public int updateCategory(CategoryDto categoryDto) {
        // 비즈니스 로직으로 구현
        // 예외처리 필요
        Category target = Objects.requireNonNull(categoryRepository.findOne(categoryDto.getId()).orElse(null));
        target.setName(categoryDto.getName());
        return 1;
    }

    // 카테고리 삭제
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public int deleteCategory(CategoryDto categoryDto) {
        return categoryRepository.deleteById(categoryDto.getId());
    }

    // 카테고리 변경사항 전체반영
    // 예외처리 필요
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
            deleteResult = deleteCategories(deleteList);
        }

        /* 수정리스트 처리 */
        if (updateList != null) {
            updateResult = updateCategories(updateList);
        }

        /* 추가리스트 처리 */
        if (insertList != null) {
            insertResult = insertCategories(insertList);
        }

        /* 모든 작업 정상처리시 결과 세팅 후 반환 */
        changeCategoryResultDto.setDone(true);
        changeCategoryResultDto.setDeleteResult(deleteResult);
        changeCategoryResultDto.setUpdateResult(updateResult);
        changeCategoryResultDto.setInsertResult(insertResult);
        return changeCategoryResultDto;
    }

    // 카테고리 여러개 추가
    private List<CategoryDto> insertCategories(List<CategoryDto> categoryDtoList) {
        if (categoryDtoList == null) {
            return null;
        }
        List<CategoryDto> result = new ArrayList<>();
        for(CategoryDto categoryDto:categoryDtoList) {
            Category newCategory = Category.createCategory(categoryDto.getName());
            categoryRepository.save(newCategory);
            result.add(categoryMapper.toDto(newCategory));
        }
        return result;
    }

    // 카테고리 여러개 수정
    private List<Integer> updateCategories(List<CategoryDto> categoryDtoList) {
        // 예외처리 필요
        if (categoryDtoList == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        for(CategoryDto categoryDto:categoryDtoList) {
            Category target = Objects.requireNonNull(categoryRepository.findOne(categoryDto.getId()).orElse(null));
            target.setName(categoryDto.getName());
            result.add(1);
        }
        return result;
    }

    // 카테고리 여러개 삭제
    private List<Integer> deleteCategories(List<CategoryDto> categoryDtoList) {
        if (categoryDtoList == null) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        for(CategoryDto categoryDto:categoryDtoList) {
            result.add(categoryRepository.deleteById(categoryDto.getId()));
        }
        return result;
    }
}
