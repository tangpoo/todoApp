package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.schedule.ScheduleListResponseDto;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Reply;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @InjectMocks
    ScheduleService scheduleService;

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
    void test1() {
        //given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("Test Title", "Test Content");

        String accessToken = "dummy_access_token";
        String username = "test_user";

        User user = User.builder()
                .username(username)
                .password("test_password")
                .email("test_email")
                .role(UserRoleEnum.USER)
                .build();

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
    @DisplayName("댓글이 있는 스케줄 조회")
    void test2() {
        //given
        Schedule schedule = Schedule.builder()
                .title("test_title")
                .content("test_content")
                .user(new User())
                .build();
        List<Reply> replies = new ArrayList<>();
        replies.add(Reply.builder().content("reply_test").schedule(schedule).build());

        String accessToken = "dummy_access_token";
        String username = "test_user";

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User()));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));
        given(replyRepository.findAllByScheduleId(any())).willReturn(replies);

        //when
        ScheduleResponseDto responseDto = scheduleService.getScheduleById("dummy_access_token", 1L);

        //then
        assertEquals(schedule.getId(), responseDto.getTodoId());
        assertNotEquals(responseDto.getReplyList().size(), 0);
    }

    @Test
    @Transactional
    @DisplayName("댓글이 없는 스케줄 조회")
    void test3() {
        //given
        User user = new User();
        Schedule schedule = Schedule.builder()
                .title("test_title")
                .content("test_content")
                .user(user)
                .build();
        List<Reply> replies = new ArrayList<>();


        String accessToken = "dummy_access_token";
        String username = "test_user";

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));
        given(replyRepository.findAllByScheduleId(any())).willReturn(replies);

        //when
        ScheduleResponseDto responseDto = scheduleService.getScheduleById("dummy_access_token", 1L);

        //then
        assertEquals(responseDto.getTodoId(), schedule.getId());
        assertEquals(responseDto.getReplyList().size(), 0);
    }

    @Test
    @Transactional
    @DisplayName("조회할 스케줄이 없는 경우")
    void test4() {
        //given
        String accessToken = "dummy_access_token";
        String username = "test_user";

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User()));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.empty());

        //when, then
        assertThrows(NoSuchElementException.class, () -> scheduleService.getScheduleById("dummy_access_token", 1L));
    }

    @Test
    @Transactional
    @DisplayName("스케줄 목록 조회")
    void test5() {
        //given
        String accessToken = "dummy_access_token";
        String username = "test_user";
        User user = User.builder()
                .username(username)
                .email("test_email")
                .password("test_password")
                .role(UserRoleEnum.USER)
                .build();
        List<Schedule> schedules = new ArrayList<>();

        Schedule schedule = Schedule.builder()
                .title("test_title")
                .content("test_content")
                .user(user)
                .build();

        schedules.add(schedule);

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findAllByUserIdOrderByIsCompletedAscModifiedAtDesc(user.getId())).willReturn(Optional.of(schedules));

        //when
        ScheduleListResponseDto responseDto = scheduleService.getSchedules(accessToken);

        //then
        assertEquals(responseDto.getSchedules().size(), 1);
        assertEquals(responseDto.getSchedules().get(0).getTodoId(), schedule.getId());
    }

    @Test
    @Transactional
    @DisplayName("스케줄 업데이트")
    void updateSchedule() {
        //given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("test_title_update", "test_content_update");
        String accessToken = "dummy_access_token";
        String username = "test_user";
        User user = User.builder()
                .username(username)
                .email("test_email")
                .password("test_password")
                .role(UserRoleEnum.USER)
                .build();
        Schedule schedule = Schedule.builder()
                .title("test_title")
                .content("test_content")
                .user(user)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));

        //when
        scheduleService.updateSchedule(accessToken, requestDto, 1L, true, true);

        //then
        assertEquals(schedule.getTitle(), requestDto.getTitle());
        assertTrue(schedule.isCompleted());
    }

    @Test
    @Transactional
    @DisplayName("스케줄 삭제")
    void deleteSchedule() {
        //given
        String accessToken = "dummy_access_token";
        String username = "test_user";
        User user = User.builder()
                .username(username)
                .email("test_email")
                .password("test_password")
                .role(UserRoleEnum.USER)
                .build();
        Schedule schedule = Schedule.builder()
                .title("test_title")
                .content("test_content")
                .user(user)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));

        //when
        scheduleService.deleteSchedule(accessToken, 1L);

        //then
        verify(scheduleRepository, times(1)).delete(schedule);
    }

    @Test
    @Transactional
    @DisplayName("스케줄 상태 변경")
    void completedSchedule() {
        //given
        String accessToken = "dummy_access_token";
        String username = "test_user";
        User user = User.builder()
                .username(username)
                .email("test_email")
                .password("test_password")
                .role(UserRoleEnum.USER)
                .build();
        Schedule schedule = Schedule.builder()
                .title("test_title")
                .content("test_content")
                .user(user)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));

        //when
        scheduleService.completedSchedule(accessToken, 1L, true, true);

        //then
        assertTrue(schedule.isCompleted());
        assertTrue(schedule.isPrivate());

    }
}
