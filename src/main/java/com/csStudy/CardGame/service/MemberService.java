package com.csStudy.CardGame.service;

import com.csStudy.CardGame.dto.MemberDto;
import com.csStudy.CardGame.dto.RegisterRequestForm;

import java.util.Optional;

public interface MemberService {
    MemberDto register(RegisterRequestForm form);
    Boolean checkExists(String email, String nickname);
}
