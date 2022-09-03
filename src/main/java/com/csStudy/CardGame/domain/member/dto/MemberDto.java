package com.csStudy.CardGame.domain.member.dto;

import com.csStudy.CardGame.domain.member.entity.Role;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private UUID id;
    private String email;
    private String nickname;
    private String password;
    Set<Role> roles;
}
