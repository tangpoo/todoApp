package com.sparta.todoapp.facade;

import com.sparta.todoapp.dto.TokenDto;
import com.sparta.todoapp.dto.user.LoginRequestDto;
import com.sparta.todoapp.dto.user.SignupRequestDto;
import com.sparta.todoapp.dto.user.SignupResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.RefreshToken;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.exceptionHandler.UnAuthorizationException;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.RefreshTokenRepository;
import com.sparta.todoapp.service.port.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberFacadeImpl implements MemberFacade {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Override
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        userService.validateDuplicateUsername(username);

        String password = passwordEncoder.encode(requestDto.getPassword());

        String email = requestDto.getEmail();
        userService.validateDuplicateEmail(email);

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

        Member savedMember = userService.save(member);

        return new SignupResponseDto(savedMember);
    }

    @Transactional
    public TokenDto login(LoginRequestDto requestDto) {
        Member member = userService.getMemberByUsername(requestDto.getUsername());

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

    @Override
    public void logout(Long memberId, String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnAuthorizationException("[ERROR] 유효하지 않은 Refresh Token 입니다.");
        }

        final String tokenUsername = jwtUtil.getClaimsFormRefreshToken(refreshToken).getSubject();
        final String entityUsername = userService.findById(memberId);

        if(!tokenUsername.equals(entityUsername)){
            throw new UnAuthorizationException("[ERROR] 로그인한 사용자의 Refresh Token 이 아닙니다.");
        }

        RefreshToken findRefreshToken = refreshTokenRepository.findByMemberId(memberId).orElseThrow(
            () -> new UnAuthorizationException("[ERROR] 이미 로그아웃 된 사용자입니다.")
        );

        refreshTokenRepository.delete(findRefreshToken);
    }
}
