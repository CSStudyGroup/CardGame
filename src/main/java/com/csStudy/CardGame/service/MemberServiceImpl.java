package com.csStudy.CardGame.service;

import com.csStudy.CardGame.domain.Member;
import com.csStudy.CardGame.domain.Role;
import com.csStudy.CardGame.dto.MemberDto;
import com.csStudy.CardGame.dto.RegisterRequestForm;
import com.csStudy.CardGame.mapper.MemberMapper;
import com.csStudy.CardGame.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Optional<MemberDto> register(RegisterRequestForm form) {
        Member newMember = Member.builder()
                .email(form.getUserEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .roles(Set.of(Role.USER))
                .build();
        return memberRepository.save(newMember)
                .map(memberMapper::toDto);
    }

    @Override
    public Optional<MemberDto> findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(memberMapper::toDto);
    }
}
