package com.sparta.todoapp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain) throws ServletException, IOException {

        String method = req.getMethod();
        String requestURI = req.getRequestURI();

        List<String> exemptedUrls = Arrays.asList(
            "/api/users/signup",
            "/api/users/login"
        );

        if ("GET".equals(method) || exemptedUrls.contains(requestURI)) {
            // GET 요청이거나 exemptedUrls에 포함된 주소로 요청된 경우
            filterChain.doFilter(req, res); // 다음 필터로 요청 전달
            return;
        }

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (!StringUtils.hasText(tokenValue)) {
            // 토큰이 전달되지 않은 경우
            sendErrorResponse(res, "토큰이 전달되지 않았습니다.", HttpStatus.BAD_REQUEST);
            return;
        }

        if (!jwtUtil.validateToken(tokenValue)) {
            // 유효하지 않은 토큰인 경우
            sendErrorResponse(res, "토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
            return;
        }

        String username = jwtUtil.getUserInfoFromToken(tokenValue);

        try {
            setAuthentication(username);
        } catch (Exception e) {
            log.error(e.getMessage());
            sendErrorResponse(res, "인증 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(req, res);
    }

    private void sendErrorResponse(HttpServletResponse res, String errorMessage, HttpStatus status) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> response = new HashMap<>();
        response.put("message", errorMessage);
        res.getWriter().write(objectMapper.writeValueAsString(response));
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }
}
