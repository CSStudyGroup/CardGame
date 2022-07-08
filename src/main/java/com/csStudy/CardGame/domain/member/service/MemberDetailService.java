package com.csStudy.CardGame.domain.member.service;

import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import com.csStudy.CardGame.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) {
        return memberRepository.findByEmail(userEmail)
                .map(MemberDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));
    }
}
