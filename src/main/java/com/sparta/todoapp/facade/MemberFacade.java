package com.sparta.todoapp.facade;

import com.sparta.todoapp.dto.TokenDto;
import com.sparta.todoapp.dto.user.LoginRequestDto;
import com.sparta.todoapp.dto.user.SignupRequestDto;
import com.sparta.todoapp.dto.user.SignupResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberFacade {

    SignupResponseDto signup(SignupRequestDto signupRequestDto);

    TokenDto login(LoginRequestDto loginRequestDto);

    void logout(Long memberId, String refreshToken);
}
