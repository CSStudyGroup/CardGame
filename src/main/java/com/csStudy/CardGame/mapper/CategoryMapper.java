package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.dto.CategoryDto;

public interface CategoryMapper {

    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category category);

}
