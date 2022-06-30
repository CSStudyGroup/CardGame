package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Member;
import com.csStudy.CardGame.domain.Role;
import com.csStudy.CardGame.dto.MemberDto;
import com.csStudy.CardGame.dto.RegisterRequestForm;
import com.csStudy.CardGame.dto.SecuredMember;
import com.csStudy.CardGame.mapper.MemberMapper;
import com.csStudy.CardGame.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

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
                .email(form.getUserEmail())
                .password(form.getPassword())
                .nickname(form.getNickname())
                .build();
        newMember.addRole(Role.USER);
        newMember.changePassword(passwordEncoder.encode(newMember.getPassword()));
        return memberMapper.toDto(memberRepository.save(newMember));
    }

    @Override
    public Boolean checkExists(String email, String nickname) {
        return memberRepository.existsByEmailOrNickname(email, nickname);
    }

}
