package com.sparta.todoapp.aop;

import com.sparta.todoapp.entity.ApiUseTime;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.jwt.UserDetailsImpl;
import com.sparta.todoapp.repository.ApiUseTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UseTimeAop {

    private final ApiUseTimeRepository apiUseTimeRepository;

    @Pointcut("execution(* com.sparta.todoapp.controller.ScheduleController.*(..))")
    private void schedule(){}

    @Pointcut("execution(* com.sparta.todoapp.controller.ReplyController.*(..))")
    private void reply(){}

    @Around("schedule() || reply()")
    public Object execute(ProceedingJoinPoint joinPoint) throws  Throwable{
        // 측정 시작 시간
        long startTime = System.currentTimeMillis();

        try{
            //핵심기능 수행
            Object output = joinPoint.proceed();
            return output;
        } finally {
            // 측정 종료 시간
            long endTime = System.currentTimeMillis();
            // 수행시간 = 종료 시간 - 시작 시간
            long runTime = endTime - startTime;

            // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class){
                // 로그인 회원 정보
                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
                Member loginUser = userDetails.getMember();

                // API 사용시간 및 DB 에 기록
                ApiUseTime apiUseTime = apiUseTimeRepository.findByMember(loginUser).orElse(null);
                if(apiUseTime == null){
                    apiUseTime = new ApiUseTime(loginUser, runTime);
                } else {
                    apiUseTime.addUseTime(runTime);
                }

                log.info("[API Use Time] Username: " + loginUser.getUsername());
                apiUseTimeRepository.save(apiUseTime);
            }
        }
    }
}
