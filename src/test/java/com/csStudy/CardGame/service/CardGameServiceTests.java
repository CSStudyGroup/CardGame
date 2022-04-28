package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.CardDto;
import com.csStudy.CardGame.dto.CategoryDto;
import com.csStudy.CardGame.mapper.CardMapper;
import com.csStudy.CardGame.mapper.CategoryMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class CardGameServiceTests {
    @Autowired
    CardGameService cardGameService;
    CardMapper cardMapper;
    CategoryMapper categoryMapper;
}
