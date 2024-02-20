package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.entity.UserRoleEnum;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ReplyRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReplyRepository replyRepository;

    @Mock
    JwtUtil jwtUtil;

    @Test
    @Transactional
    @DisplayName("스케줄 생성, 조회")
    void test1(){
        //given
        ScheduleService scheduleService = new ScheduleService(scheduleRepository, userRepository, replyRepository, jwtUtil);
        ScheduleRequestDto requestDto = new ScheduleRequestDto("Test Title", "Test Content");

        String accessToken = "dummy_access_token";
        String username = "test_user";
        User user = new User(username, "Test password", "Test email", UserRoleEnum.USER);
        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        Long scheduleId = 1L;
        Schedule schedule = Schedule.builder()
                .id(scheduleId)
                .title("Test Title")
                .content("Test Content")
                .user(user)
                .build();

        //when
        given(scheduleRepository.save(any())).willReturn(schedule);
        ScheduleResponseDto responseDto = scheduleService.createSchedule(accessToken, requestDto);

        //then
        assertNotNull(responseDto);
        assertEquals(scheduleId, responseDto.getTodoId());
        assertEquals("Test Title", responseDto.getTitle());
        assertEquals("Test Content", responseDto.getContent());
        assertFalse(responseDto.isCompleted());
        assertFalse(responseDto.isPrivate());
        assertEquals(username, responseDto.getAuthor());
    }


    @Test
    @Transactional
    @DisplayName("토큰과 스케줄 id로 스케줄 조회")
    void test2() {
        //given
        ScheduleService scheduleService = new ScheduleService(scheduleRepository, userRepository, replyRepository, jwtUtil);

        //when

        //then
    }

    @Test
    void getScheduleById() {
    }

    @Test
    void getSchedules() {
    }

    @Test
    void updateSchedule() {
    }

    @Test
    void deleteSchedule() {
    }

    @Test
    void completedSchedule() {
    }
}
