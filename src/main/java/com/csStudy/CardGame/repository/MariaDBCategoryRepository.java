package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("mariadb_category")
public class MariaDBCategoryRepository implements CategoryRepository {

    private final EntityManager em;

    @Autowired
    public MariaDBCategoryRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Category category) {
        if (category.getId() == null) {
            em.persist(category);
        }
        else {
            em.merge(category);
        }
    }

    @Override
    public int delete(Category category) {
        try {
            em.remove(category);
            return 1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int deleteById(Long id) {
        // 예외처리 필요
        try {
            em.createQuery("delete from Category c where c.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            return 1;
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteByIdIn(Set<Long> idSet) {
        try {
            em.createQuery("delete from Category c where c.id in :idSet")
                    .setParameter("idSet", idSet)
                    .executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Category> findOne(Long id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    @Override
    public Optional<Category> findByName(String name) {
        return Optional.ofNullable(em.createQuery("select distinct c from Category c join fetch c.cards where c.name = :name", Category.class)
                .setParameter("name", name)
                .getSingleResult());
    }

    public Optional<List<Category>> findByIdIn(Set<Long> idSet) {
        return Optional.ofNullable(em.createQuery("select c from Category c where c.id in :idSet", Category.class)
                .setParameter("idSet", idSet)
                .getResultList());
    }

    @Override
    public List<Category> findAll() {
        return em.createQuery("select c from Category c", Category.class)
                .getResultList();
    }

}
