package com.csStudy.CardGame.domain.member.controller;

import com.csStudy.CardGame.domain.member.dto.EditMemberInfoForm;
import com.csStudy.CardGame.domain.member.dto.MemberDto;
import com.csStudy.CardGame.domain.member.dto.RegisterRequestForm;
import com.csStudy.CardGame.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable UUID memberId) {
        return ResponseEntity
                .ok()
                .body(memberService.findMemberById(memberId));
    }

    @PostMapping("/members")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestForm form) {
        memberService.register(form);
        return ResponseEntity
                .ok()
                .body(null);
    }

    @GetMapping("/members/validity")
    public ResponseEntity<Boolean> checkRegisterValidation(@RequestParam String email, @RequestParam String nickname) {
        return ResponseEntity
                .ok()
                .body(!memberService.checkExists(email, nickname));
    }

    @PutMapping("/members")
    public ResponseEntity<Void> editMemberInfo(@RequestBody EditMemberInfoForm editMemberInfoForm) {
        memberService.editMemberInfo(editMemberInfoForm);
        return ResponseEntity
                .ok()
                .body(null);
    }
}
