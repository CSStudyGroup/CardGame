package com.csStudy.CardGame.domain.category.mapper;

import com.csStudy.CardGame.domain.category.dto.NewCategory;
import com.csStudy.CardGame.domain.category.entity.Category;
import com.csStudy.CardGame.domain.category.dto.SimpleCategory;

public interface CategoryMapper {
    Category toEntity(NewCategory newCategory);

    SimpleCategory toSimpleCategory(Category category);
}
