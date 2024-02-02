package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.security.UserDetailsImpl;
import com.sparta.todoapp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/new")
    public ResponseEntity<Void> createSchedule(@RequestBody ScheduleRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        scheduleService.createSchedule(requestDto, userDetails.getUser());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
