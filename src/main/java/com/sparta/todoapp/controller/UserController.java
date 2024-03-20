package com.sparta.todoapp.controller;

import static com.sparta.todoapp.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.sparta.todoapp.jwt.JwtUtil.REFRESH_TOKEN;
import static com.sparta.todoapp.message.UserMessage.LOGIN_SUCCESS;
import static com.sparta.todoapp.message.UserMessage.LOGOUT_SUCCESS;
import static com.sparta.todoapp.message.UserMessage.SIGN_UP_API;
import static com.sparta.todoapp.message.UserMessage.SIGN_UP_SUCCESS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.dto.TokenDto;
import com.sparta.todoapp.dto.user.LoginRequestDto;
import com.sparta.todoapp.dto.user.SignupRequestDto;
import com.sparta.todoapp.dto.user.SignupResponseDto;
import com.sparta.todoapp.facade.MemberFacade;
import com.sparta.todoapp.jwt.UserDetailsImpl;
import com.sparta.todoapp.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final MemberFacade memberFacade;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    @Operation(summary = SIGN_UP_API)
    public ResponseEntity<ResponseDto<SignupResponseDto>> signup(
        @RequestBody @Valid SignupRequestDto requestDto) {
        log.info(SIGN_UP_API);
        return ResponseEntity.ok()
            .body(ResponseDto.<SignupResponseDto>builder()
                .message(SIGN_UP_SUCCESS)
                .data(memberFacade.signup(requestDto))
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(@RequestBody LoginRequestDto requestDto) {
        TokenDto token = memberFacade.login(requestDto);

        return ResponseEntity.ok()
            .header(AUTHORIZATION_HEADER, token.getAccessToken())
            .header(REFRESH_TOKEN, token.getRefreshToken())
            .body(ResponseDto.<Void>builder()
                .message(LOGIN_SUCCESS)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestHeader(REFRESH_TOKEN) String refreshToken){

        log.info("로그아웃 API");
        memberFacade.logout(userDetails.getMember().getId(), refreshToken);

        return ResponseEntity.ok()
            .header(AUTHORIZATION_HEADER, (String) null)
            .body(ResponseDto.<Void>builder()
                .message(LOGOUT_SUCCESS)
                .build());
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<ResponseDto<TokenDto>> kakaoLogin(@RequestParam String code, HttpServletResponse response)
        throws JsonProcessingException {
        TokenDto token = kakaoService.kakaoLogin(code);

        return ResponseEntity.ok()
            .header(AUTHORIZATION_HEADER, token.getAccessToken())
            .header(REFRESH_TOKEN, token.getRefreshToken())
            .body(ResponseDto.<TokenDto>builder()
                .message(LOGIN_SUCCESS)
                .build());
    }
}
