package com.csStudy.CardGame.repository;

import com.csStudy.CardGame.domain.Member;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> save(Member member);
    Optional<Member> findByEmail(String userEmail);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findOne(Long id);
}
