package com.csStudy.CardGame.domain.category.mapper;

import com.csStudy.CardGame.domain.category.dto.NewCategory;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.SimpleCategory;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper{

    @Override
    public Category toEntity(NewCategory newCategory) {
        return Category.builder()
                .name(newCategory.getName())
                .cardCount(newCategory.getCardCount())
                .build();
    }

    @Override
    public SimpleCategory toSimpleCategory(Category category) {
        if (category == null) {
            return null;
        }
        else {
            return SimpleCategory.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .cardCount(category.getCardCount())
                    .build();
        }
    }

}
