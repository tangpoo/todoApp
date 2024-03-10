package com.sparta.todoapp.service;

import com.sparta.todoapp.common.ScheduleTest;
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
import java.time.MonthDay;
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
public class ScheduleServiceTest implements ScheduleTest {

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
    @DisplayName("스케줄 생성, 조회")
    void test1() {
        //given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("Test Title", "Test Content");

        String accessToken = TEST_TOKEN;
        String username = TEST_USER_NAME;

        User user = User.builder()
                .username(username)
                .password(TEST_USER_PASSWORD)
                .email(TEST_USER_EMAIL)
                .role(UserRoleEnum.USER)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        Long scheduleId = 1L;
        Schedule schedule = Schedule.builder()
                .id(scheduleId)
                .title(TEST_SCHEDULE_TITLE)
                .content(TEST_SCHEDULE_CONTENT)
                .user(user)
                .build();

        //when
        given(scheduleRepository.save(any(Schedule.class))).willReturn(schedule);
        ScheduleResponseDto responseDto = scheduleService.createSchedule(accessToken, requestDto);

        //then
        assertNotNull(responseDto);
        assertEquals(scheduleId, responseDto.getTodoId());
        assertEquals(TEST_SCHEDULE_TITLE, responseDto.getTitle());
        assertEquals(TEST_SCHEDULE_CONTENT, responseDto.getContent());
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
                .title(TEST_SCHEDULE_TITLE)
                .content(TEST_SCHEDULE_CONTENT)
                .user(new User())
                .build();
        List<Reply> replies = new ArrayList<>();
        replies.add(Reply.builder().content("reply_test").schedule(schedule).build());

        String username = TEST_USER_NAME;

        given(jwtUtil.getUserInfoFromToken(TEST_TOKEN)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User()));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));
        given(replyRepository.findAllByScheduleId(any())).willReturn(replies);

        //when
        ScheduleResponseDto responseDto = scheduleService.getScheduleById(TEST_TOKEN, TEST_USER_ID);

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
                .title(TEST_SCHEDULE_TITLE)
                .content(TEST_SCHEDULE_CONTENT)
                .user(user)
                .build();
        List<Reply> replies = new ArrayList<>();


        String username = TEST_USER_NAME;

        given(jwtUtil.getUserInfoFromToken(TEST_TOKEN)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));
        given(replyRepository.findAllByScheduleId(any())).willReturn(replies);

        //when
        ScheduleResponseDto responseDto = scheduleService.getScheduleById(TEST_TOKEN, TEST_SCHEDULE_ID);

        //then
        assertEquals(responseDto.getTodoId(), schedule.getId());
        assertNull(responseDto.getReplyList());
    }

    @Test
    @Transactional
    @DisplayName("스케줄 조회 실패")
    void test4() {
        //given
        String username = TEST_USER_NAME;

        given(jwtUtil.getUserInfoFromToken(TEST_TOKEN)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(new User()));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> scheduleService.getScheduleById(TEST_TOKEN, TEST_USER_ID));
    }

//    @Test
//    @Transactional
//    @DisplayName("스케줄 목록 조회")
//    void test5() {
//        //given
//        String accessToken = TEST_TOKEN;
//        String username = TEST_USER_NAME;
//        User user = User.builder()
//                .username(username)
//                .email(TEST_USER_EMAIL)
//                .password(TEST_USER_PASSWORD)
//                .role(UserRoleEnum.USER)
//                .build();
//        List<Schedule> schedules = new ArrayList<>();
//
//        Schedule schedule = Schedule.builder()
//                .title(TEST_SCHEDULE_TITLE)
//                .content(TEST_SCHEDULE_CONTENT)
//                .user(user)
//                .build();
//
//        schedules.add(schedule);
//
//        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
//        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
//        given(scheduleRepository.findAllByUserIdOrderByIsCompletedAscModifiedAtDesc(user.getId())).willReturn(Optional.of(schedules));
//
//        //when
//        ScheduleListResponseDto responseDto = scheduleService.getSchedules(accessToken);
//
//        //then
//        assertEquals(responseDto.getSchedules().size(), 1);
//        assertEquals(responseDto.getSchedules().get(0).getTodoId(), schedule.getId());
//    }

    @Test
    @Transactional
    @DisplayName("스케줄 업데이트")
    void updateSchedule() {
        //given
        ScheduleRequestDto requestDto = new ScheduleRequestDto("test_title_update", "test_content_update");
        String accessToken = TEST_TOKEN;
        String username = TEST_USER_NAME;
        User user = User.builder()
                .username(username)
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .role(UserRoleEnum.USER)
                .build();
        Schedule schedule = Schedule.builder()
                .title(TEST_SCHEDULE_TITLE)
                .content(TEST_SCHEDULE_CONTENT)
                .user(user)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));

        //when
        scheduleService.updateSchedule(accessToken, requestDto, TEST_SCHEDULE_ID, true, true);

        //then
        assertEquals(schedule.getTitle(), requestDto.getTitle());
        assertTrue(schedule.isCompleted());
    }

    @Test
    @Transactional
    @DisplayName("스케줄 삭제")
    void deleteSchedule() {
        //given
        String accessToken = TEST_TOKEN;
        String username = TEST_USER_NAME;
        User user = User.builder()
                .username(username)
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .role(UserRoleEnum.USER)
                .build();
        Schedule schedule = Schedule.builder()
                .title(TEST_SCHEDULE_TITLE)
                .content(TEST_SCHEDULE_CONTENT)
                .user(user)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));

        //when
        scheduleService.deleteSchedule(accessToken, TEST_SCHEDULE_ID);

        //then
        verify(scheduleRepository, times(1)).delete(schedule);
    }

    @Test
    @Transactional
    @DisplayName("스케줄 상태 변경")
    void completedSchedule() {
        //given
        String accessToken = TEST_TOKEN;
        String username = TEST_USER_NAME;
        User user = User.builder()
                .username(username)
                .email(TEST_USER_EMAIL)
                .password(TEST_USER_PASSWORD)
                .role(UserRoleEnum.USER)
                .build();
        Schedule schedule = Schedule.builder()
                .title(TEST_SCHEDULE_TITLE)
                .content(TEST_SCHEDULE_CONTENT)
                .user(user)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdAndUserId(any(), any())).willReturn(Optional.of(schedule));

        //when
        scheduleService.completedSchedule(accessToken, TEST_SCHEDULE_ID, true, true);

        //then
        assertTrue(schedule.isCompleted());
        assertTrue(schedule.isPrivate());

    }
}
