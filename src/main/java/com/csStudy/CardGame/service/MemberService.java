package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.MemberDto;
import com.csStudy.CardGame.dto.RegisterRequestForm;

import java.util.Optional;

public interface MemberService {
    Optional<MemberDto> register(RegisterRequestForm form);
    Optional<MemberDto> findByEmail(String email);
    Optional<MemberDto> findByNickname(String nickname);
    Optional<MemberDto> findOne(Long id);
}
