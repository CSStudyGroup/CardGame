package com.csStudy.CardGame.domain.member.service;

import com.csStudy.CardGame.domain.member.dto.MemberDetails;
import com.csStudy.CardGame.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) {
        MemberDetails target = memberRepository.findByEmail(userEmail)
                .map((member) -> {
                    return MemberDetails.builder()
                            .id(member.getId())
                            .nickname(member.getNickname())
                            .email(member.getEmail())
                            .password(member.getPassword())
                            .authorities(member.getRoles().stream()
                                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                                    .collect(Collectors.toSet()))
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));
        return target;
    }
}
