package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository("mariadb_category")
public class MariaDBCategoryRepository implements CategoryRepository {

    private final EntityManager em;

    @Autowired
    public MariaDBCategoryRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Category> findById(int id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    @Override
    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    @Override
    public Category insert(Category category) {
        em.persist(category);
        return category;
    }

    @Override
    public int deleteById(int id) {
        try {
            findById(id)
                    .ifPresentOrElse(em::remove, () -> {
                        throw new NoSuchElementException();
                    });
            return 1;
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }

    @Override
    public int updateById(Category category) {
        try {
            findById(category.getCid())
                    .ifPresentOrElse(target -> target.setCname(category.getCname()), () -> {
                        throw new NoSuchElementException();
                    });
            return 1;
        }
        catch (NoSuchElementException e) {
            return 0;
        }
    }



}
