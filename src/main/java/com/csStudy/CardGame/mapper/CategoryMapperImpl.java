package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.dto.CategoryDto;

public class CategoryMapperImpl implements CategoryMapper{

    @Override
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        else {
            return CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .cardCount(category.getCardCount())
                    .build();
        }
    }

}
