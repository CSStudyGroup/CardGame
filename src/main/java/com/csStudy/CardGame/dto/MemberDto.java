package com.csStudy.CardGame.dto;

import com.csStudy.CardGame.domain.Role;
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
