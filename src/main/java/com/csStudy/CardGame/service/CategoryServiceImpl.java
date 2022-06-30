package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.dto.CategoryDetail;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import com.csStudy.CardGame.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CardMapper cardMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper,
                               CardMapper cardMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.cardMapper = cardMapper;
    }

    @Override
    @Transactional
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDetail> getAllCategoriesDetail() {
        return categoryRepository.findDetailAll().stream()
                .map((category) -> {
                    return CategoryDetail.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .cardCount(category.getCardCount())
                            .cards(category.getCards().stream()
                                    .map(cardMapper::toDto)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDto> getSelectedCategories(Collection<Long> categoryIdSet) {
        return categoryRepository.findByIdIn(categoryIdSet).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDetail> getSelectedCategoriesDetail(Collection<Long> categoryIdSet) {
        return categoryRepository.findDetailByIdIn(categoryIdSet).stream()
                .map((category) -> {
                    return CategoryDetail.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .cardCount(category.getCardCount())
                            .cards(category.getCards().stream()
                                    .map(cardMapper::toDto)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto getCategoryById(Long id) {
        Category target = categoryRepository.findById(id).orElse(null);
        return target == null ? null : categoryMapper.toDto(target);
    }

    @Override
    @Transactional
    public CategoryDetail getCategoryDetailById(Long id) {
        Category target = categoryRepository.findDetailOne(id).orElse(null);
        return target == null ? null : CategoryDetail.builder()
                .id(target.getId())
                .name(target.getName())
                .cardCount(target.getCardCount())
                .cards(target.getCards().stream()
                        .map(cardMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    // 카테고리 변경사항 전체반영
    // 예외처리 필요
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public boolean changeCategories(List<CategoryDto> insertedList, List<CategoryDto> updatedList, Set<Long> deletedList) {

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
    private boolean insertCategories(List<CategoryDto> insertedCategoryList) {
        if (insertedCategoryList.isEmpty()) {
            return false;
        }
        categoryRepository.saveAll(insertedCategoryList.stream()
                .map(Category::createCategory)
                .collect(Collectors.toList()));
        return true;
    }

    // 카테고리 여러개 수정
    private boolean updateCategories(List<CategoryDto> updatedCategoryList) {
        // 예외처리 필요
        if (updatedCategoryList.isEmpty()) {
            return false;
        }
        Map<Long, Category> targets = new HashMap<>();
        categoryRepository.findByIdIn(updatedCategoryList.stream()
                .map(CategoryDto::getId)
                .collect(Collectors.toList()))
                .forEach((category) -> targets.put(category.getId(), category));
        for (CategoryDto categoryDto: updatedCategoryList) {
            if (targets.containsKey(categoryDto.getId())) {
                targets.get(categoryDto.getId()).setName(categoryDto.getName());
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
