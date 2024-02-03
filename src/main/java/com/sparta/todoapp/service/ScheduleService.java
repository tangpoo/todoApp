package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ScheduleResponseDto createSchedule(String accessToken, ScheduleRequestDto requestDto) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();
        Schedule schedule = new Schedule(requestDto, user);

        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public ScheduleResponseDto getScheduleById(Long id) {
        Schedule schedule = findSchedule(id);

        return new ScheduleResponseDto(schedule);
    }

    private Schedule findSchedule(Long id){
        return scheduleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("일정이 존재하지 않습니다.")
        );
    }
}
