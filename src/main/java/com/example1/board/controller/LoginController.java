package com.example1.board.controller;


import com.example1.board.entity.OauthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login/oauth2/code/kakao")
public class LoginController {
    @GetMapping
    public @ResponseBody String kakaoCallback(String code){
//        데이터 요청 라이브러리
        RestTemplate rt = new RestTemplate();
//        포스트 요청 보낼때 데이터를 설명해주는 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//        포스트 요청 body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "ecf7e443a56627d29bfcc53a5fa9e752");
        params.add("redirect_uri", "http://localhost:8082/login/oauth2/code/kakao");
        params.add("code", code);
        //헤더와 body 합치기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoTokenRequest, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            // JSON 처리 중 예외가 발생한 경우 처리
            e.printStackTrace(); // 또는 로깅 등의 작업을 수행할 수 있음
        }
        System.out.println("카카오 엑세스 토큰 :" + oauthToken.getAccess_token());

//        개인정보 받아오기
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

// "Bearer " 뒤에 공백 추가
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
// 띄어쓰기 추가
        headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me", // 개인정보 요청 주소
                HttpMethod.POST, // 요청할 방식
                kakaoProfileRequest2, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        return response2.getBody();

    }

}
