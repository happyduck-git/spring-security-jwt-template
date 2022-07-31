package com.ggshin.jwtdemo2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "<h1>token</h1>";
    }

    //'/token' 일 때와 동일하게 POST 요청이므로 같은 방식으로 동작함
    @PostMapping("/token2")
    public String token2() {
        return "<h1>token2</h1>";
    }
}
