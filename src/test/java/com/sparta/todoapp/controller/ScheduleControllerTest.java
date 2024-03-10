package com.sparta.todoapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.common.ScheduleTest;
import com.sparta.todoapp.dto.schedule.ScheduleListResponseDto;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ScheduleController.class)
@WithMockUser
public class ScheduleControllerTest implements ScheduleTest {

    @MockBean
    ScheduleService scheduleService;

    User user;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        user = User.builder()
                .username(TEST_USER_NAME)
                .password(TEST_USER_PASSWORD)
                .email(TEST_USER_EMAIL)
                .role(UserRoleEnum.USER)
                .build();
    }

//    @Test
//    @DisplayName("스케줄 생성 요청1")
//    void test0() throws Exception {
//        //given
//
//        //when
//        var action = mvc.perform(post("/api/schedule/new")       // ResponseDto null 값, 메서드 정의 필요
//                .header("Authorization", TEST_TOKEN)
//                .content(objectMapper.writeValueAsString(TEST_SCHEDULE_REQUEST_DTO))
//                .contentType(MediaType.APPLICATION_JSON)
//                .with(csrf())
//                .accept(MediaType.APPLICATION_JSON));
//
//        //then
//        action.andExpect(status().isCreated());
//        verify(scheduleService, times(1)).createSchedule(eq(TEST_TOKEN), TEST_SCHEDULE_REQUEST_DTO);
//    }

    @Test
    @DisplayName("스케줄 생성 요청2")
    void test1() throws Exception {
        //given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("test_title", "test_content");
        String accessToken = TEST_TOKEN;
        Schedule schedule = Schedule.builder()
                .title(TEST_SCHEDULE_TITLE)
                .content(TEST_SCHEDULE_CONTENT)
                .user(user)
                .build();
        ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);

        given(scheduleService.createSchedule(eq(accessToken), any(ScheduleRequestDto.class))).willReturn(responseDto);

        //when + then
        mvc.perform(post("/api/schedule/new")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.message").value("스케줄 작성 성공"));
    }

    @Test
    @DisplayName("스케줄 조회 요청")
    void test2() throws Exception {
        //given
        given(scheduleService.getScheduleById(eq(TEST_TOKEN), eq(TEST_SCHEDULE_ID))).willReturn(TEST_SCHEDULE_RESPONSE_DTO);

        //when
        var action = mvc.perform(get("/api/schedule/{id}", TEST_SCHEDULE_ID)
                .header("Authorization", TEST_TOKEN)
                .accept(MediaType.APPLICATION_JSON));

        //then
        action
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.title").value(TEST_SCHEDULE_TITLE));
//                .andExpect(jsonPath("$.content").value(TEST_SCHEDULE_CONTENT));
    }

    @Test
    @DisplayName("스케줄 목록 조회 요청")
    void test3() throws Exception {
        //given
        TEST_SCHEDULE_LIST.add(TEST_SCHEDULE_RESPONSE_DTO);
        TEST_SCHEDULE_LIST.add(TEST_ANOTHER_SCHEDULE_RESPONSE_DTO);
        ScheduleListResponseDto responseDto = new ScheduleListResponseDto(TEST_USER_NAME, TEST_SCHEDULE_LIST);

        given(scheduleService.getSchedules(eq(TEST_TOKEN))).willReturn(responseDto);

        //when
        var action = mvc.perform(get("/api/schedule/schedules", TEST_TOKEN)
                .header("Authorization", TEST_TOKEN)
                .accept(MediaType.APPLICATION_JSON));

        //then
        action
                .andExpect(status().isOk());
    }
}
