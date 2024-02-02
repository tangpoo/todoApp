package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.service.ScheduleService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.todoapp.jwt.JwtUtil.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/new")
    public ResponseEntity<Void> createSchedule(@RequestBody ScheduleRequestDto requestDto, @CookieValue(AUTHORIZATION_HEADER) Cookie cookie){
        scheduleService.createSchedule(requestDto, cookie);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
