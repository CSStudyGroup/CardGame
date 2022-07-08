package com.csStudy.CardGame.domain.category.mapper;

import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;

public interface CategoryMapper {
    CategoryDto toDto(Category category);
}
