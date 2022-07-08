package com.csStudy.CardGame.domain.member.mapper;

import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.dto.MemberDto;

public interface MemberMapper {
    Member toEntity(MemberDto memberDto);
    MemberDto toDto(Member member);
}
