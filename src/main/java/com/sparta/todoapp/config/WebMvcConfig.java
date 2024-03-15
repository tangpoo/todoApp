//package com.sparta.todoapp.config;
//
//import com.sparta.todoapp.jwt.JwtTokenInterceptor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    private final JwtTokenInterceptor jwtTokenInterceptor;
//
//    public void addInterceptors(InterceptorRegistry registry){
//        log.info("인터셉터 등록");
//        registry.addInterceptor(jwtTokenInterceptor)
//            .addPathPatterns("/**")
//            .excludePathPatterns("/signup");
//    }
//}
