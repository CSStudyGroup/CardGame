package com.csStudy.CardGame.domain.category.mapper;

import com.csStudy.CardGame.domain.category.dto.CategoryDtoWithDetail;
import com.csStudy.CardGame.domain.category.dto.NewCategoryForm;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper{

    @Override
    public Category toEntity(NewCategoryForm newCategoryForm) {
        return Category.builder()
                .name(newCategoryForm.getName())
                .build();
    }

    @Override
    public CategoryDto toCategoryDto(Category category) {
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

    @Override
    public CategoryDtoWithDetail toCategoryDtoWithDetail(Category category) {
        return CategoryDtoWithDetail.builder()
                .id(category.getId())
                .name(category.getName())
                .cardCount(category.getCardCount())
                .ownerId(category.getOwner().getId())
                .ownerNickname(category.getOwner().getNickname())
                .ownerEmail(category.getOwner().getEmail())
                .build();
    }

}
