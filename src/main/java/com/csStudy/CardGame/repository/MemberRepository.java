package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<Member> findByEmail(String userEmail);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmailOrNickname(String userEmail, String nickname);
}
