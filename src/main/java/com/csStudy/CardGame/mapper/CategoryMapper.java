package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Category;
import com.csStudy.CardGame.dto.CategoryDetail;
import com.csStudy.CardGame.dto.CategoryDto;

public interface CategoryMapper {
    CategoryDto toDto(Category category);
}
