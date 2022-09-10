package com.csStudy.CardGame.domain.member.repository;

import com.csStudy.CardGame.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByEmail(String userEmail);

    @EntityGraph(attributePaths = "roles")
    @Query("select m from Member m where m.email = :userEmail")
    Optional<Member> findByEmailWithRole(@Param("userEmail") String userEmail);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmailOrNickname(String userEmail, String nickname);
}
