package com.csStudy.CardGame.domain.member.dto;

import com.csStudy.CardGame.domain.member.entity.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String nickname;
    private String password;
    Set<Role> roles;
}
