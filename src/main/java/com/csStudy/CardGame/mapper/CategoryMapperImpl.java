package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.dto.CategoryDto;

public class CategoryMapperImpl implements CategoryMapper{

    @Override
    public Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        else {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setName(categoryDto.getName());
            category.setCardCount(categoryDto.getCardCount());
            return category;
        }
    }

    @Override
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        else {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryDto.setCardCount(category.getCardCount());
            return categoryDto;
        }
    }

}
