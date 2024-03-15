package com.sparta.todoapp.controller;

import static com.sparta.todoapp.message.ScheduleMessage.CREATE_SCHEDULE_API;
import static com.sparta.todoapp.message.ScheduleMessage.CREATE_SCHEDULE_SUCCESS;
import static com.sparta.todoapp.message.ScheduleMessage.DELETE_SCHEDULE_API;
import static com.sparta.todoapp.message.ScheduleMessage.GET_SCHEDULES_API;
import static com.sparta.todoapp.message.ScheduleMessage.GET_SCHEDULES_SUCCESS;
import static com.sparta.todoapp.message.ScheduleMessage.GET_SCHEDULE_API;
import static com.sparta.todoapp.message.ScheduleMessage.GET_SCHEDULE_SUCCESS;
import static com.sparta.todoapp.message.ScheduleMessage.PATCH_SCHEDULE_API;
import static com.sparta.todoapp.message.ScheduleMessage.PATCH_SCHEDULE_CHECK_API;
import static com.sparta.todoapp.message.ScheduleMessage.PATCH_SCHEDULE_CHECK_DESCRIPTION;
import static com.sparta.todoapp.message.ScheduleMessage.PATCH_SCHEDULE_SUCCESS;
import static com.sparta.todoapp.message.ScheduleMessage.SEARCH_SCHEDULE_API;
import static com.sparta.todoapp.message.ScheduleMessage.SEARCH_SCHEDULE_SUCCESS;

import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.dto.ResponsePageDto;
import com.sparta.todoapp.dto.SearchListDto;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.facade.ScheduleFacade;
import com.sparta.todoapp.jwt.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleFacade scheduleFacade;

    @PostMapping("/new")
    @Operation(summary = CREATE_SCHEDULE_API)
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> createSchedule(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody ScheduleRequestDto requestDto) {
        log.info(CREATE_SCHEDULE_API);

        ScheduleResponseDto scheduleResponseDto = scheduleFacade.createSchedule(
            userDetails.getMember(),
            requestDto);

        return ResponseEntity.created(createUri(scheduleResponseDto.getTodoId()))
            .body(ResponseDto.<ScheduleResponseDto>builder()
                .message(CREATE_SCHEDULE_SUCCESS)
                .data(scheduleResponseDto)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = GET_SCHEDULE_API)
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> getScheduleById(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long id) {
        log.info(GET_SCHEDULE_API);

        return ResponseEntity.ok()
            .body(ResponseDto.<ScheduleResponseDto>builder()
                .message(GET_SCHEDULE_SUCCESS)
                .data(scheduleFacade.getScheduleById(userDetails.getMember(), id))
                .build());
    }

    @GetMapping
    @Operation(summary = GET_SCHEDULE_API)
    public ResponseEntity<ResponsePageDto<ScheduleResponseDto>> getSchedules(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        Pageable pageable) {
        log.info(GET_SCHEDULES_API);

        return ResponseEntity.ok()
            .body(ResponsePageDto.<ScheduleResponseDto>builder()
                .message(GET_SCHEDULES_SUCCESS)
                .data(scheduleFacade.getSchedules(userDetails.getMember(), pageable))
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<SearchListDto<ScheduleResponseDto>> getSearchSchedule(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam("type") String type,
        @RequestParam("keyword") String keyword,
        Pageable pageable
    ) {
        log.info(SEARCH_SCHEDULE_API);

        return ResponseEntity.ok()
            .body(SearchListDto.<ScheduleResponseDto>builder()
                .dataList(scheduleFacade.getSearchSchedule(userDetails.getMember(), type, keyword,
                    pageable))
                .message(SEARCH_SCHEDULE_SUCCESS)
                .build());
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = PATCH_SCHEDULE_API)
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> updateSchedule(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody ScheduleRequestDto requestDto,
        @PathVariable Long id,
        @RequestParam(required = false) boolean isCompleted,
        @RequestParam(required = false) boolean isPrivate
    ) {
        log.info(PATCH_SCHEDULE_API);

        ScheduleResponseDto scheduleResponseDto = scheduleFacade.updateSchedule(
            userDetails.getMember(),
            requestDto,
            id,
            isCompleted,
            isPrivate
        );

        return ResponseEntity.created(updateUri())
            .body(ResponseDto.<ScheduleResponseDto>builder()
                .message(PATCH_SCHEDULE_SUCCESS)
                .data(scheduleResponseDto)
                .build());
    }

    @PatchMapping("/completed/{id}")
    @Operation(summary = PATCH_SCHEDULE_CHECK_API, description = PATCH_SCHEDULE_CHECK_DESCRIPTION)
    public ResponseEntity<Void> completedSchedule(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long id,
        @RequestParam(required = false) boolean isCompleted,
        @RequestParam(required = false) boolean isPrivate
    ) {
        log.info(PATCH_SCHEDULE_CHECK_API);

        scheduleFacade.completedSchedule(userDetails.getMember(), id, isCompleted, isPrivate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = DELETE_SCHEDULE_API)
    public ResponseEntity<Void> deleteSchedule(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long id) {
        log.info(DELETE_SCHEDULE_API);

        scheduleFacade.deleteSchedule(userDetails.getMember(), id);

        return ResponseEntity.noContent().build();
    }

    private URI createUri(Long scheduleId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(scheduleId)
            .toUri();
    }

    private URI updateUri() {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .replaceQueryParam("isCompleted")
            .replaceQueryParam("isPrivate")
            .build()
            .toUri();
    }
}
