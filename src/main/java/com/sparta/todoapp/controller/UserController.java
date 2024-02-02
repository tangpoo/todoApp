package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.dto.UserInfoDto;
import com.sparta.todoapp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult){
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0){
            for (FieldError fieldError : fieldErrors) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //todo ExceptionHandler 로 핸들링
        System.out.println("requestDto.getPassword() = " + requestDto.getPassword());

        userService.signup(requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse res){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
