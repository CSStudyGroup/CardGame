package com.csStudy.CardGame.dto;

import com.csStudy.CardGame.domain.Member;
import com.csStudy.CardGame.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class SecuredMember extends User {

    private static final String rolePrefix = "ROLE_";
    private final Long id;

    public SecuredMember(Member member) {
        super(member.getEmail(), member.getPassword(), toGrantedAuthority(member.getRoles()));
        this.id = member.getId();
    }

    private static List<GrantedAuthority> toGrantedAuthority(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(rolePrefix + role.getRoleName()))
                .collect(Collectors.toList());
    }
}
