package com.csStudy.CardGame.domain.category.mapper;

import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
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
