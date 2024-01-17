package com.example1.board.controller;


import com.example1.board.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequiredArgsConstructor

public class LoginController {
    private final LoginService loginService;
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
        System.out.println("code 받음");
        String kakaoResponse = loginService.getKakaoUserInfo(code);
        System.out.println("유저 정보 넘김");
        return ResponseEntity.ok(kakaoResponse);
    }
}
