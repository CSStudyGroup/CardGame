package com.csStudy.CardGame.mapper;

import com.csStudy.CardGame.domain.Member;
import com.csStudy.CardGame.dto.MemberDto;

public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member toEntity(MemberDto memberDto) {
        return Member.builder()
                .id(memberDto.getId())
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .roles(memberDto.getRoles())
                .build();
    }

    @Override
    public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRoles())
                .build();
    }
}
