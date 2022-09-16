package com.csStudy.CardGame.domain.member.dto;

import com.csStudy.CardGame.domain.member.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class EditMemberInfoForm {
    private UUID id;
    private String email;
    private String nickname;
    private Set<Role> roles;
}
