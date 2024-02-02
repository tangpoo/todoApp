package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ScheduleRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private ScheduleRepository scheduleRepository;
    private JwtUtil jwtUtil;

//    public void createSchedule(ScheduleRequestDto requestDto, Cookie cookie) {
//
//        Schedule schedule = scheduleRepository.save(new Schedule(requestDto));
//    }
}
