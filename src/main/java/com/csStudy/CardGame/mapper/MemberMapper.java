package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Member;
import com.csStudy.CardGame.dto.MemberDto;

public interface MemberMapper {
    Member toEntity(MemberDto memberDto);
    MemberDto toDto(Member member);
}
