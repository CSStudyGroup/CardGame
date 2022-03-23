package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.annotation.Target;
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
    public Optional<Category> findByName(String name) {
        return Optional.ofNullable(em.createQuery("select c from Category c where c.cname = :name", Category.class)
                .setParameter("name", name)
                .getSingleResult());
    }

    @Override
    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    @Override
    public Category insert(Category category) {
        try {
            em.persist(category);
            return category;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Category category) {
        try {
            findById(category.getCid())
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
    public int update(Category category) {
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
