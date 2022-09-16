package com.csStudy.CardGame.domain.member.mapper;

import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.dto.MemberDto;
import org.springframework.stereotype.Component;

@Component
public class MemberMapperImpl implements MemberMapper {
    @Override
    public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .roles(member.getRoles())
                .build();
    }
}
