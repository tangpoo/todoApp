package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ResponseDto;
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

import static com.sparta.todoapp.message.UserMessage.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = SIGN_UP_API)
    public ResponseEntity<ResponseDto<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        log.info(SIGN_UP_API);
        return ResponseEntity.ok()
                .body(ResponseDto.<SignupResponseDto>builder()
                        .message(SIGN_UP_SUCCESS)
                        .data(userService.signup(requestDto))
                        .build());
    }

    @PostMapping("/login")
    @Operation(summary = LOGIN_API)
    public ResponseEntity<ResponseDto<Void>> login(@RequestBody LoginRequestDto requestDto) {
        log.info(LOGIN_API);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, userService.login(requestDto))
                .body(ResponseDto.<Void>builder()
                        .message(LOGIN_SUCCESS)
                        .build());
    }
}
