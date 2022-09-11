package com.csStudy.CardGame.domain.category.service;

import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithOwnerInfo;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;
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
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDtoWithOwnerInfo> getAllCategoriesDetail() {
        return categoryRepository.findDetailAll().stream()
                .map(categoryMapper::toCategoryDtoWithOwnerInfo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDto> getSelectedCategories(Collection<Long> categoryIdSet) {
        return categoryRepository.findByIdIn(categoryIdSet).stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CategoryDtoWithOwnerInfo> getSelectedCategoriesDetail(Collection<Long> categoryIdSet) {
        return categoryRepository.findDetailByIdIn(categoryIdSet).stream()
                .map(categoryMapper::toCategoryDtoWithOwnerInfo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto getCategoryById(Long id) {
        Category target = categoryRepository.findById(id).orElseThrow(
                () -> ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        null
                )
        );
        return categoryMapper.toCategoryDto(target);
    }

    @Override
    @Transactional
    public CategoryDtoWithOwnerInfo getCategoryDetailById(Long id) {
        Category target = categoryRepository.findDetailOne(id).orElseThrow(
                () -> ApiErrorException.createException(
                        ApiErrorEnums.RESOURCE_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        null,
                        null
                )
        );
        return categoryMapper.toCategoryDtoWithOwnerInfo(target);
    }

    // 카테고리 변경사항 전체반영
    // 예외처리 필요
    @CacheEvict(value = "categoryList", allEntries = true)
    @Transactional
    public boolean changeCategories(List<NewCategoryForm> insertedList, List<CategoryDto> updatedList, Set<Long> deletedList) {

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
    private boolean insertCategories(List<NewCategoryForm> insertedCategoryList) {
        if (insertedCategoryList.isEmpty()) {
            return false;
        }
        Member owner = memberRepository.findByEmail(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName()
        ).orElseThrow(() -> ApiErrorException.createException(
                ApiErrorEnums.INVALID_ACCESS,
                HttpStatus.UNAUTHORIZED,
                null,
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
        for (CategoryDto categoryDto : updatedCategoryList) {
            if (targets.containsKey(categoryDto.getId())) {
                targets.get(categoryDto.getId()).changeName(categoryDto.getName());
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
