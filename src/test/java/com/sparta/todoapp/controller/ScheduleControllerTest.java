package com.sparta.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.common.CommonTest;
import com.sparta.todoapp.common.TodoTest;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest implements TodoTest {

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

    @Test
    @DisplayName("스케줄 생성 요청")
    @WithMockUser
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("스케줄 작성 성공"));
    }
}
