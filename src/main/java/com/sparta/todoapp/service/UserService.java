package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.TokenDto;
import com.sparta.todoapp.dto.user.LoginRequestDto;
import com.sparta.todoapp.dto.user.SignupRequestDto;
import com.sparta.todoapp.dto.user.SignupResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.RefreshToken;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.MemberRepository;
import com.sparta.todoapp.repository.RefreshTokenRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<Member> checkUsername = memberRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DataIntegrityViolationException("중복된 사용자가 존재합니다.");
        }

        String email = requestDto.getEmail();
        Optional<Member> checkEmail = memberRepository.findByEmail(email);
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

        Member member = Member.builder()
            .username(username)
            .password(password)
            .email(email)
            .role(role)
            .build();

        memberRepository.save(member);

        return new SignupResponseDto(member);
    }

    @Transactional
    public TokenDto login(LoginRequestDto requestDto) {
        Member member = memberRepository.findByUsername(requestDto.getUsername())
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createToken(member);
        String refreshToken = jwtUtil.createRefreshToken(member);
        RefreshToken tokenEntity = RefreshToken.builder()
            .refreshToken(refreshToken)
            .member(member)
            .build();

        refreshTokenRepository.findByMemberId(member.getId()).ifPresentOrElse(
            (findTokenPair) -> findTokenPair.refreshUpdate(refreshToken),
            () -> refreshTokenRepository.save(tokenEntity)
        );

        return new TokenDto(accessToken, refreshToken);
    }
}
