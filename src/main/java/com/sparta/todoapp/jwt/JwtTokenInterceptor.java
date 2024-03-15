//package com.sparta.todoapp.jwt;
//
//import static com.sparta.todoapp.jwt.JwtUtil.AUTHORIZATION_HEADER;
//import static com.sparta.todoapp.jwt.JwtUtil.REFRESH_TOKEN;
//
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//@RequiredArgsConstructor
//public class JwtTokenInterceptor implements HandlerInterceptor {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
//        String accessToken = request.getHeader(AUTHORIZATION_HEADER);
//        String refreshToken = request.getHeader(REFRESH_TOKEN);
//
//        if(accessToken != null){
//            if(jwtUtil.validateToken(accessToken)){
//                return true;
//            }
//        }
//
//        response.setStatus(401);
//        response.setHeader(AUTHORIZATION_HEADER, accessToken);
//        response.setHeader(REFRESH_TOKEN, refreshToken);
//        response.setHeader("msg", "Check the tokens.");
//        return false;
//    }
//}
