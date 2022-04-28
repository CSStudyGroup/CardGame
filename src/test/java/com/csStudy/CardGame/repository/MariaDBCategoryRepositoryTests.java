package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
public class MariaDBCategoryRepositoryTests {
    @Autowired
    @Qualifier("mariadb_category")
    CategoryRepository repository;

}
