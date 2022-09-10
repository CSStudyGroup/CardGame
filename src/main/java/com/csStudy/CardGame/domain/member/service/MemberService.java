package com.csStudy.CardGame.domain.member.service;

import com.csStudy.CardGame.domain.member.dto.MemberDto;
import com.csStudy.CardGame.domain.member.dto.RegisterRequestForm;

import java.util.UUID;

public interface MemberService {
    MemberDto register(RegisterRequestForm form);
    Boolean checkExists(String email, String nickname);
    MemberDto findMemberById(UUID memberId);
}
