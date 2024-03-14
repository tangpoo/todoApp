package com.sparta.todoapp.controller;

import static com.sparta.todoapp.message.UserMessage.LOGIN_API;
import static com.sparta.todoapp.message.UserMessage.LOGIN_SUCCESS;
import static com.sparta.todoapp.message.UserMessage.SIGN_UP_API;
import static com.sparta.todoapp.message.UserMessage.SIGN_UP_SUCCESS;

import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.dto.TokenDto;
import com.sparta.todoapp.dto.TokenRequestDto;
import com.sparta.todoapp.dto.user.LoginRequestDto;
import com.sparta.todoapp.dto.user.SignupRequestDto;
import com.sparta.todoapp.dto.user.SignupResponseDto;
import com.sparta.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = SIGN_UP_API)
    public ResponseEntity<ResponseDto<SignupResponseDto>> signup(
        @RequestBody @Valid SignupRequestDto requestDto) {
        log.info(SIGN_UP_API);
        return ResponseEntity.ok()
            .body(ResponseDto.<SignupResponseDto>builder()
                .message(SIGN_UP_SUCCESS)
                .data(userService.signup(requestDto))
                .build());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto<Void>> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        TokenDto token = userService.reissue(tokenRequestDto);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, token.getAccessToken())
            .header("X-Refresh-Token", token.getRefreshToken())
            .body(ResponseDto.<Void>builder()
                .message(LOGIN_SUCCESS)
                .build());
    }
}
