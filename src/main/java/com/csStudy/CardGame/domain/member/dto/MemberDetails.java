package com.csStudy.CardGame.domain.member.dto;

import com.csStudy.CardGame.domain.member.entity.Member;
import com.csStudy.CardGame.domain.member.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MemberDetails extends User {

    private static final String rolePrefix = "ROLE_";
    private final Long id;
    private final String nickname;
    private final String email;

    public MemberDetails(Member member) {
        super(member.getEmail(), member.getPassword(), toGrantedAuthority(member.getRoles()));
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }

    private static List<GrantedAuthority> toGrantedAuthority(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(rolePrefix + role.getRoleName()))
                .collect(Collectors.toList());
    }
}
