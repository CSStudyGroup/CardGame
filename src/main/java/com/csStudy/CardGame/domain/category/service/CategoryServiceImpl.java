package com.csStudy.CardGame.domain.category.service;

import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithDetail;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;
import com.csStudy.CardGame.domain.category.mapper.CategoryMapper;
import com.csStudy.CardGame.domain.category.repository.CategoryRepository;
import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.repository.MemberRepository;
import com.csStudy.CardGame.exception.ApiErrorEnums;
import com.csStudy.CardGame.exception.ApiErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            MemberRepository memberRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDto addCategory(NewCategoryForm newCategoryForm) {
        Member owner = memberRepository.findById(
                ((MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getId()
        ).orElseThrow(
                () -> ApiErrorException.createException(
                        ApiErrorEnums.INVALID_ACCESS,
                        HttpStatus.UNAUTHORIZED,
                        null,
                        "존재하지 않는 Member 로 인증됨"
                )
        );
        try {
            Category insertedCategory = categoryRepository.save(
                    Category.builder()
                            .name(newCategoryForm.getName())
                            .owner(owner)
                            .build()
            );

            return categoryMapper.toCategoryDto(insertedCategory);
        }
        catch (DataIntegrityViolationException ex) {
            throw ApiErrorException.createException(
                    ApiErrorEnums.RESOURCE_CONFLICT,
                    HttpStatus.CONFLICT,
                    null,
                    null
            );
        }
    }

    @Override
    @Transactional
    public void editCategory(CategoryDto categoryDto) {
        MemberDetails member = (MemberDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Category target = categoryRepository
                .findByIdWithDetail(categoryDto.getId())
                .orElseThrow(
                        () -> ApiErrorException.createException(
                                ApiErrorEnums.RESOURCE_NOT_FOUND,
                                HttpStatus.NOT_FOUND,
                                null,
                                null
                        )
                );

        if (!member.getId().equals(target.getOwner().getId())) {
            throw ApiErrorException.createException(
                    ApiErrorEnums.INVALID_ACCESS,
                    HttpStatus.FORBIDDEN,
                    null,
                    null
            );
        }
        target.changeName(categoryDto.getName());
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        return categoryMapper.toCategoryDto(categoryRepository
                .findById(categoryId)
                .orElseThrow(
                        () -> ApiErrorException.createException(
                                ApiErrorEnums.RESOURCE_NOT_FOUND,
                                HttpStatus.NOT_FOUND,
                                null,
                                null
                        )
                ));
    }

    @Override
    public CategoryDtoWithDetail getCategoryWithDetail(Long categoryId) {
        return categoryMapper.toCategoryDtoWithDetail(
                categoryRepository.findByIdWithDetail(categoryId).orElseThrow(
                        () -> ApiErrorException.createException(
                                    ApiErrorEnums.RESOURCE_NOT_FOUND,
                                    HttpStatus.NOT_FOUND,
                                    null,
                                    null
                        )
                )
        );
    }

    @Override
    public List<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository
                .findAll(pageable)
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getCategories(String keyword, Pageable pageable) {
        return categoryRepository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDtoWithDetail> getAllCategoriesWithDetail(Pageable pageable) {
        return categoryRepository
                .findAllWithDetail(pageable)
                .stream()
                .map(categoryMapper::toCategoryDtoWithDetail)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDtoWithDetail> getCategoriesWithDetail(String keyword, Pageable pageable) {
        return categoryRepository
                .findByNameContainingIgnoreCaseWithDetail(keyword, pageable)
                .stream()
                .map(categoryMapper::toCategoryDtoWithDetail)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        MemberDetails member = (MemberDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Category target = categoryRepository
                .findByIdWithDetail(categoryId)
                .orElseThrow(
                        () -> ApiErrorException.createException(
                                ApiErrorEnums.RESOURCE_NOT_FOUND,
                                HttpStatus.NOT_FOUND,
                                null,
                                null
                        )
                );

        if (!member.getId().equals(target.getOwner().getId())) {
            throw ApiErrorException.createException(
                    ApiErrorEnums.INVALID_ACCESS,
                    HttpStatus.FORBIDDEN,
                    null,
                    null
            );
        }

        if (target.getCardCount() > 0) {
            throw ApiErrorException.createException(
                    ApiErrorEnums.REQUEST_DENIED,
                    HttpStatus.BAD_REQUEST,
                    "비어있지 않은 카테고리는 삭제할 수 없습니다.",
                    null
            );
        }
        else {
            categoryRepository.delete(target);
        }
    }
}
