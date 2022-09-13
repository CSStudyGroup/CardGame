package com.csStudy.CardGame.domain.category.mapper;

import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithDetail;
import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;

public interface CategoryMapper {
    Category toEntity(NewCategoryForm newCategoryForm);

    CategoryDto toCategoryDto(Category category);

    CategoryDtoWithDetail toCategoryDtoWithDetail(Category category);
}
