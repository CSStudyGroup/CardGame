package com.csStudy.CardGame.domain.member.service;

import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.entity.Role;
import com.csStudy.CardGame.domain.member.dto.MemberDto;
import com.csStudy.CardGame.domain.member.dto.RegisterRequestForm;
import com.csStudy.CardGame.domain.member.mapper.MemberMapper;
import com.csStudy.CardGame.domain.member.repository.MemberRepository;
import com.csStudy.CardGame.exception.ApiErrorEnums;
import com.csStudy.CardGame.exception.ApiErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                             MemberMapper memberMapper,
                             PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public MemberDto register(RegisterRequestForm form) {
        Member newMember = Member.builder()
                .id(UUID.randomUUID())
                .email(form.getUserEmail())
                .password(form.getPassword())
                .nickname(form.getNickname())
                .build();
        newMember.addRole(Role.ROLE_USER);
        newMember.changePassword(passwordEncoder.encode(newMember.getPassword()));
        return memberMapper.toDto(memberRepository.save(newMember));
    }

    @Override
    public Boolean checkExists(String email, String nickname) {
        return memberRepository.existsByEmailOrNickname(email, nickname);
    }

    @Override
    public MemberDto findMemberById(UUID memberId) {
        return memberMapper.toDto(memberRepository.findById(memberId).orElseThrow(() -> ApiErrorException.createException(
                ApiErrorEnums.RESOURCE_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                null
        )));
    }
}
