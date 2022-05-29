package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class MariaDBMemberRepository implements MemberRepository {
    private final EntityManager em;

    @Autowired
    public MariaDBMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Member> save(Member member) {
        em.persist(member);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult());
    }

    @Override
    public Optional<Member> findOne(Long id) {
        return Optional.ofNullable(em.find(Member.class,id));
    }
}
