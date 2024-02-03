package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.service.ScheduleService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sparta.todoapp.jwt.JwtUtil.AUTHORIZATION_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/new")
    public ResponseEntity<ResponseDto> createSchedule(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestBody ScheduleRequestDto requestDto){
        log.info("할일카드 작성");

        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(accessToken, requestDto);
        return ResponseEntity.ok().body(new ResponseDto("할일카드 작성 성공", scheduleResponseDto));
    }

    @GetMapping("/schedule/{id}")
    public ResponseEntity<ResponseDto> getScheduleById(@PathVariable Long id){
        log.info("할일카드 조회");

        ScheduleResponseDto scheduleResponseDto = scheduleService.getScheduleById(id);
        return ResponseEntity.ok().body(new ResponseDto("할일카드 조회 성공", scheduleResponseDto));
    }

    @GetMapping("/schedules")
    public ResponseEntity<ResponseDto> getSchedules(@RequestParam(required = false) String title){
        log.info("할일카드 목록 조회");

        return ResponseEntity.ok()
                .body(new ResponseDto("할일카도 목록 조회 성공", scheduleService.getSchedules(title)));
    }
}
