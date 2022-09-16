package com.csStudy.CardGame.domain.member.mapper;

import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.dto.MemberDto;

public interface MemberMapper {
    MemberDto toDto(Member member);
}
