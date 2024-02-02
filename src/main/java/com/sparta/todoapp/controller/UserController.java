package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.dto.SignupResponseDto;
import com.sparta.todoapp.dto.UserInfoDto;
import com.sparta.todoapp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto){
        log.info("회원 가입");
        return ResponseEntity.ok().body(new ResponseDto("회원가입 성공", userService.signup(requestDto)));
    }

    @PostMapping("/user/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginRequestDto requestDto){
        log.info("로그인");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, userService.login(requestDto))
                .body(new ResponseDto("로그인 성공"));
    }
}
