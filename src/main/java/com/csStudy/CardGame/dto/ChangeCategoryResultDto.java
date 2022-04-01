package com.csStudy.CardGame.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ChangeCategoryResultDto {
    private boolean done;
    private List<CategoryDto> insertResult;
    private List<Integer> updateResult;
    private List<Integer> deleteResult;
}
