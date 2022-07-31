package com.ggshin.jwtdemo2.controller;

import com.ggshin.jwtdemo2.model.Member;
import com.ggshin.jwtdemo2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @GetMapping("/api/v1/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @GetMapping("/api/v1/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/api/v1/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @PostMapping("/token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody Member member) {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRoles("ROLE_USER");
        memberRepository.save(member);
        return "회원가입 완료";
    }
}
