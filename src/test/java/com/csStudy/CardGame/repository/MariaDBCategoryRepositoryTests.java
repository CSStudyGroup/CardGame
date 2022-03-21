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
    @Qualifier("mariadb_category") CategoryRepository repository;

    // insert, findById, findAll 테스트
    @Test
    public void insert() {
        List<Category> expected = repository.findAll();

        // insert
        Category category = new Category();
        category.setCname("database");
        repository.insert(category);

        Category category2 = new Category();
        category2.setCname("network");
        repository.insert(category2);

        Category category3 = new Category();
        category3.setCname("operating system");
        repository.insert(category3);

        // findById
        Assertions.assertThat(repository.findById(category.getCid()).get()).isEqualTo(category);
        Assertions.assertThat(repository.findById(category2.getCid()).get()).isEqualTo(category2);
        Assertions.assertThat(repository.findById(category3.getCid()).get()).isEqualTo(category3);

        // findAll
        expected.add(category);
        expected.add(category2);
        expected.add(category3);

        Assertions.assertThat(repository.findAll()).isEqualTo(expected);
    }

    // updateById 테스트
    @Test
    public void updateById() {
        // insert
        Category category = new Category();
        category.setCname("database");
        repository.insert(category);

        Category category2 = new Category();
        category2.setCid(category.getCid());
        category2.setCname("network");

        repository.update(category2);


        Assertions.assertThat(repository.findById(category.getCid()).get().getCname()).isEqualTo("network");
    }

    // deleteById 테스트
    @Test
    public void deleteById() {
        // insert
        Category category = new Category();
        category.setCname("database");
        repository.insert(category);

        int targetCid = category.getCid();
        repository.delete(category);
        Assertions.assertThat(repository.findById(targetCid).orElse(null)).isEqualTo(null);
    }
}
