package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.dto.ScheduleListResponseDto;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.sparta.todoapp.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.sparta.todoapp.message.ScheduleMessage.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/new")
    @Operation(summary = CREATE_SCHEDULE_API)
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> createSchedule(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestBody ScheduleRequestDto requestDto){
        log.info(CREATE_SCHEDULE_API);

        ScheduleResponseDto scheduleResponseDto = scheduleService.createSchedule(accessToken, requestDto);

        return ResponseEntity.created(createUri(scheduleResponseDto.getTodoId()))
                .body(ResponseDto.<ScheduleResponseDto>builder()
                        .message(CREATE_SCHEDULE_SUCCESS)
                        .data(scheduleResponseDto)
                        .build());
    }

    @GetMapping("/schedule/{id}")
    @Operation(summary = GET_SCHEDULE_API)
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> getScheduleById(
            @RequestHeader(value = "Authorization") String accessToken,
            @PathVariable Long id){
        log.info(GET_SCHEDULE_API);

        return ResponseEntity.ok()
                .body(ResponseDto.<ScheduleResponseDto>builder()
                        .message(GET_SCHEDULE_SUCCESS)
                        .data(scheduleService.getScheduleById(accessToken, id))
                        .build());
    }

    @GetMapping("/schedules")
    @Operation(summary = SEARCH_SCHEDULE_API)
    public ResponseEntity<ResponseDto<ScheduleListResponseDto>> getSchedules(
            @RequestHeader(value = "Authorization") String accessToken){
        log.info(SEARCH_SCHEDULE_API);

        return ResponseEntity.ok()
                .body(ResponseDto.<ScheduleListResponseDto>builder()
                        .message(SEARCH_SCHEDULE_SUCCESS)
                        .data(scheduleService.getSchedules(accessToken))
                        .build());
    }

    @PatchMapping("/schedule/update/{id}")
    @Operation(summary = PATCH_SCHEDULE_API)
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> updateSchedule(
            @RequestHeader(value = "Authorization") String accessToken,
            @Valid @RequestBody ScheduleRequestDto requestDto,
            @PathVariable Long id,
            @RequestParam(required = false) boolean isCompleted,
            @RequestParam(required = false) boolean isPrivate
    ){
        log.info(PATCH_SCHEDULE_API);

        ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(accessToken, requestDto, id, isCompleted, isPrivate);

        return ResponseEntity.created(updateUri())
                .body(ResponseDto.<ScheduleResponseDto>builder()
                        .message(PATCH_SCHEDULE_SUCCESS)
                        .data(scheduleResponseDto)
                        .build());
    }

    @PatchMapping("/schedule/completed/{id}")
    @Operation(summary = PATCH_SCHEDULE_CHECK_API, description = PATCH_SCHEDULE_CHECK_DESCRIPTION)
    public ResponseEntity<Void> completedSchedule(
            @RequestHeader(value = "Authorization") String accessToken,
            @PathVariable Long id,
            @RequestParam(required = false) boolean isCompleted,
            @RequestParam(required = false) boolean isPrivate
    ){
        log.info(PATCH_SCHEDULE_CHECK_API);

        scheduleService.completedSchedule(accessToken, id, isCompleted, isPrivate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/schedule/delete/{id}")
    @Operation(summary = DELETE_SCHEDULE_API)
    public ResponseEntity<Void> deleteSchedule(
            @RequestHeader(value = "Authorization") String accessToken,
            @PathVariable Long id){
        log.info(DELETE_SCHEDULE_API);

        scheduleService.deleteSchedule(accessToken, id);

        return ResponseEntity.noContent().build();
    }

    private URI createUri(Long scheduleId){
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(scheduleId)
                .toUri();
    }

    private URI updateUri(){
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam("isCompleted")
                .replaceQueryParam("isPrivate")
                .build()
                .toUri();
    }
}
