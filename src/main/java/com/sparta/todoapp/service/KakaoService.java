package com.sparta.todoapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.dto.KakaoUserInfoDto;
import com.sparta.todoapp.dto.TokenDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.port.MemberRepository;
import java.net.URI;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;


    public TokenDto kakaoLogin(String code) throws JsonProcessingException {
        String accessToken = getToken(code);

        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        Member kakaoUser = registerKakaoMemberIfNeeded(kakaoUserInfo);

        String createToken = jwtUtil.createToken(kakaoUser);
        String refreshToken = jwtUtil.createRefreshToken(kakaoUser);

        return new TokenDto(createToken, refreshToken);
    }

    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth/kakao.com")
            .path("/oauth/token")
            .encode()
            .build()
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "b4626ccb733c781d8ad14274b2ee867f");
        body.add("redirect_uri", "http://localhost:8080/api/users/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException{
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com")
            .path("/v2/user/me")
            .encode()
            .build()
            .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
            .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private Member registerKakaoMemberIfNeeded(KakaoUserInfoDto kakaoUserInfoDto){
        Long kakaoId = kakaoUserInfoDto.getId();
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId).orElse(null);

        if(kakaoUser == null){
            String kakaoEmail = kakaoUserInfoDto.getEmail();
            Member sameEmailUser = memberRepository.findByEmail(kakaoEmail).orElse(null);
            if(sameEmailUser != null){
                kakaoUser = sameEmailUser;
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                String email = kakaoUserInfoDto.getEmail();

                kakaoUser = Member.builder()
                    .username(kakaoUserInfoDto.getNickname())
                    .password(encodedPassword)
                    .email(email)
                    .role(UserRoleEnum.USER)
                    .kakaoId(kakaoId)
                    .build();
            }

            memberRepository.save(kakaoUser);
        }

        return kakaoUser;
    }
}
