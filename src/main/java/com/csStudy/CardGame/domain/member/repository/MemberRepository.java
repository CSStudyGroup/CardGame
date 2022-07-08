package com.csStudy.CardGame.domain.member.repository;

import com.csStudy.CardGame.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<Member> findByEmail(String userEmail);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmailOrNickname(String userEmail, String nickname);
}
