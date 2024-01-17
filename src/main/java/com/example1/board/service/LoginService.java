package com.example1.board.service;

import com.example1.board.entity.OauthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
@Service
public class LoginService {
    private final static String KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_PROFILE_URL = "https://kapi.kakao.com/v2/user/me";
    private final ObjectMapper objectMapper;
    @Autowired
    public LoginService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getKakaoUserInfo(String code) {
        String accessToken = requestKakaoAccessToken(code);
        System.out.println(code);
        // Kakao 사용자 정보 요청
        return requestKakaoUserProfile(accessToken);
    }

    private String requestKakaoUserProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USER_PROFILE_URL,
                HttpMethod.POST,
                request,
                String.class
        );
        return response.getBody();
    }

    private String requestKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "ecf7e443a56627d29bfcc53a5fa9e752");
        params.add("redirect_uri", "http://localhost:3000/login/oauth2/callback/kakao");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_OAUTH_TOKEN_URL,
                HttpMethod.POST,
                request,
                String.class
        );

        try {
            OauthToken oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
            return oauthToken.getAccess_token();
        } catch (JsonProcessingException e) {
            // JSON 처리 중 예외가 발생한 경우 예외 처리 로직 추가
            e.printStackTrace();
            return null; // 적절한 오류 처리를 수행해야 합니다.
        }
    }


}
