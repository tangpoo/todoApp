package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.user.LoginRequestDto;
import com.sparta.todoapp.dto.user.SignupRequestDto;
import com.sparta.todoapp.dto.user.SignupResponseDto;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DataIntegrityViolationException("중복된 사용자가 존재합니다.");
        }

        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new DataIntegrityViolationException("중복된 Email 입니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new AccessDeniedException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = User.builder()
                        .username(username)
                                .password(password)
                                        .email(email)
                                                .role(role)
                                                        .build();

        userRepository.save(user);

        return new SignupResponseDto(user);
    }

    public String login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("등록된 회원이 없습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getUsername(), user.getRole());
    }
}
