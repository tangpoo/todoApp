package com.sparta.todoapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.config.WebConfig;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

    @Mock
    ScheduleService scheduleService;

    User user;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    void setup() {
        user = User.builder()
                .username("test_username")
                .password("test_password")
                .email("test_email")
                .role(UserRoleEnum.USER)
                .build();
    }

    @Test
    @DisplayName("스케줄 생성 요청")
    @WithMockUser
    void test1() throws Exception {
        //given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("test_title", "test_content");
        String accessToken = "dummyAccessToken";
        Schedule schedule = Schedule.builder()
                .title("test_title")
                .content("test_content")
                .user(user)
                .build();
        ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);

        given(scheduleService.createSchedule(eq(accessToken), any(ScheduleRequestDto.class))).willReturn(responseDto);

        //when + then
        mvc.perform(post("/api/schedule/newA")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.massage").value("스케줄 작성 성공"))
                .andExpect(jsonPath("$.data").exists());

    }
}
