package com.csStudy.CardGame.domain.category.mapper;

import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithOwnerInfo;
import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;

public interface CategoryMapper {
    Category toEntity(NewCategoryForm newCategoryForm);

    CategoryDto toCategoryDto(Category category);

    CategoryDtoWithOwnerInfo toCategoryDtoWithOwnerInfo(Category category);
}
