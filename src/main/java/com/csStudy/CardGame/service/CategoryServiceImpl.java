package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.dto.CategoryDetail;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import com.csStudy.CardGame.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        return categoryRepository.findAll().orElseGet(ArrayList::new).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDetail> getAllCategoriesDetail() {
        return categoryRepository.findDetailAll().orElseGet(ArrayList::new).stream()
                .map((category) -> {
                    return CategoryDetail.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .cardCount(category.getCardCount())
                            .cardDtoList(category.getCards().stream()
                                    .map(cardMapper::toDto)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDto> getSelectedCategories(Collection<Long> categoryIdSet) {
        return categoryRepository.findByIdIn(categoryIdSet).orElseGet(ArrayList::new).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDetail> getSelectedCategoriesDetail(Collection<Long> categoryIdSet) {
        return categoryRepository.findDetailByIdIn(categoryIdSet).orElseGet(ArrayList::new).stream()
                .map((category) -> {
                    return CategoryDetail.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .cardCount(category.getCardCount())
                            .cardDtoList(category.getCards().stream()
                                    .map(cardMapper::toDto)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto getCategoryById(Long id) {
        Category target = categoryRepository.findOne(id).orElse(null);
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
                .cardDtoList(target.getCards().stream()
                        .map(cardMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

}
