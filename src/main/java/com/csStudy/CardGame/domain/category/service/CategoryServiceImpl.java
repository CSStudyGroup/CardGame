package com.csStudy.CardGame.domain.category.service;

import com.csStudy.CardGame.domain.category.dto.NewCategory;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.DetailCategory;
import com.csStudy.CardGame.domain.category.dto.SimpleCategory;
import com.csStudy.CardGame.domain.card.mapper.CardMapper;
import com.csStudy.CardGame.domain.category.mapper.CategoryMapper;
import com.csStudy.CardGame.domain.category.repository.CategoryRepository;
import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.repository.MemberRepository;
import com.csStudy.CardGame.exception.ApiErrorEnums;
import com.csStudy.CardGame.exception.ApiErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final CategoryMapper categoryMapper;
    private final CardMapper cardMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               MemberRepository memberRepository,
                               CategoryMapper categoryMapper,
                               CardMapper cardMapper) {
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.categoryMapper = categoryMapper;
        this.cardMapper = cardMapper;
    }

    @Override
    @Transactional
    public List<SimpleCategory> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toSimpleCategory)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DetailCategory> getAllCategoriesDetail() {
        return categoryRepository.findDetailAll().stream()
                .map((category) -> DetailCategory.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .cardCount(category.getCardCount())
                            .cards(category.getCards().stream()
                                    .map(cardMapper::toDetailCard)
                                    .collect(Collectors.toList()))
                            .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SimpleCategory> getSelectedCategories(Collection<Long> categoryIdSet) {
        return categoryRepository.findByIdIn(categoryIdSet).stream()
                .map(categoryMapper::toSimpleCategory)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DetailCategory> getSelectedCategoriesDetail(Collection<Long> categoryIdSet) {
        return categoryRepository.findDetailByIdIn(categoryIdSet).stream()
                .map((category) -> DetailCategory.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .cardCount(category.getCardCount())
                            .cards(category.getCards().stream()
                                    .map(cardMapper::toDetailCard)
                                    .collect(Collectors.toList()))
                            .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SimpleCategory getCategoryById(Long id) {
        Category target = categoryRepository.findById(id).orElse(null);
        return target == null ? null : categoryMapper.toSimpleCategory(target);
    }

    @Override
    @Transactional
    public DetailCategory getCategoryDetailById(Long id) {
        Category target = categoryRepository.findDetailOne(id).orElse(null);
        return target == null ? null : DetailCategory.builder()
                .id(target.getId())
                .name(target.getName())
                .cardCount(target.getCardCount())
                .cards(target.getCards().stream()
                        .map(cardMapper::toDetailCard)
                        .collect(Collectors.toList()))
                .build();
    }

    // 카테고리 변경사항 전체반영
    // 예외처리 필요
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public boolean changeCategories(List<NewCategory> insertedList, List<SimpleCategory> updatedList, Set<Long> deletedList) {

        /* 삭제리스트 처리 */
        if (!deletedList.isEmpty()) {
            deleteCategories(deletedList);
        }

        /* 수정리스트 처리 */
        if (!updatedList.isEmpty()) {
            updateCategories(updatedList);
        }

        /* 추가리스트 처리 */
        if (!insertedList.isEmpty()) {
            insertCategories(insertedList);
        }

        return true;
    }

    // 카테고리 여러개 추가
    private boolean insertCategories(List<NewCategory> insertedCategoryList) {
        if (insertedCategoryList.isEmpty()) {
            return false;
        }
        Member owner = memberRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ).orElseThrow(() -> ApiErrorException.createException(
                ApiErrorEnums.RESOURCE_NOT_FOUND,
                HttpStatus.UNAUTHORIZED,
                null
        ));
        categoryRepository.saveAll(insertedCategoryList.stream()
                .map((dto) -> {
                    Category newCategory = categoryMapper.toEntity(dto);
                    newCategory.changeOwner(owner);
                    return newCategory;
                })
                .collect(Collectors.toList()));
        return true;
    }

    // 카테고리 여러개 수정
    private boolean updateCategories(List<SimpleCategory> updatedCategoryList) {
        // 예외처리 필요
        if (updatedCategoryList.isEmpty()) {
            return false;
        }
        Map<Long, Category> targets = new HashMap<>();
        categoryRepository.findByIdIn(updatedCategoryList.stream()
                .map(SimpleCategory::getId)
                .collect(Collectors.toList()))
                .forEach((category) -> targets.put(category.getId(), category));
        for (SimpleCategory simpleCategory : updatedCategoryList) {
            if (targets.containsKey(simpleCategory.getId())) {
                targets.get(simpleCategory.getId()).changeName(simpleCategory.getName());
            }
        }
        return true;
    }

    // 카테고리 여러개 삭제
    private boolean deleteCategories(Set<Long> deletedCategorySet) {
        if (deletedCategorySet.isEmpty()) {
            return false;
        }
        categoryRepository.deleteAllByIdInBatch(deletedCategorySet);
        return true;
    }
}
