package com.csStudy.CardGame.dto;

import com.csStudy.CardGame.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    Set<Role> roles;
}
