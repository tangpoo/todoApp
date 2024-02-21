package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.reply.ReplyRequestDto;
import com.sparta.todoapp.dto.reply.ReplyResponseDto;
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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

    @InjectMocks
    ReplyService replyService;

    @Mock
    UserRepository userRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ReplyRepository replyRepository;

    @Mock
    JwtUtil jwtUtil;

    @Test
    @Transactional
    @DisplayName("댓글 생성 성공")
    void test1() {
        //given
        ReplyRequestDto requestDto = new ReplyRequestDto();
        requestDto.setContent("test_content");
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
        Reply reply = Reply.builder()
                .content(requestDto.getContent())
                .user(user)
                .schedule(schedule)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(scheduleRepository.findById(any())).willReturn(Optional.of(schedule));
        given(replyRepository.save(any(Reply.class))).willReturn(reply);

        //when
        ReplyResponseDto responseDto = replyService.createReply(accessToken, requestDto, 1L);

        //then
        assertEquals(reply.getContent(), responseDto.getContent());
    }

    @Test
    @Transactional
    @DisplayName("댓글 생성 유저 조회 실패")
    void test2() {
        ReplyRequestDto requestDto = new ReplyRequestDto();
        requestDto.setContent("test_content");
        String accessToken = "dummy_access_token";
        String username = "test_user";

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> replyService.createReply(accessToken, requestDto, 1L));
    }

    @Test
    @Transactional
    @DisplayName("댓글 수정 성공")
    void test3() {
        //given
        ReplyRequestDto requestDto = new ReplyRequestDto();
        requestDto.setContent("test_content");
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
        Reply reply = Reply.builder()
                .id(1L)
                .content(requestDto.getContent())
                .user(user)
                .schedule(schedule)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(replyRepository.findById(any())).willReturn(Optional.of(reply));
        given(scheduleRepository.findById(any())).willReturn(Optional.of(schedule));

        //when
        ReplyResponseDto responseDto = replyService.updateReply(accessToken, requestDto, 1L, 1L);

        //then
        assertEquals(responseDto.getContent(), requestDto.getContent());
        assertEquals(responseDto.getContent(), reply.getContent());
    }

    @Test
    @Transactional
    @DisplayName("댓글 수정 실패")
    void test4() {
        //given
        ReplyRequestDto requestDto = new ReplyRequestDto();
        requestDto.setContent("test_content");
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
        given(replyRepository.findById(any())).willReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> replyService.updateReply(accessToken, requestDto, 1L, 1L));
    }


    @Test
    @Transactional
    @DisplayName("댓글 수정 실패")
    void test5() {
        //given
        ReplyRequestDto requestDto = new ReplyRequestDto();
        requestDto.setContent("test_content");
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
        Reply reply = Reply.builder()
                .id(1L)
                .content(requestDto.getContent())
                .user(user)
                .schedule(schedule)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(replyRepository.findById(any())).willReturn(Optional.of(reply));
        given(scheduleRepository.findById(any())).willReturn(Optional.empty());

        //when + then
        assertThrows(NoSuchElementException.class, () -> replyService.updateReply(accessToken, requestDto, 1L, 1L));
    }

    @Test
    @Transactional
    @DisplayName("댓글 삭제")
    void test6() {
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
        Reply reply = Reply.builder()
                .id(1L)
                .content("test_content")
                .user(user)
                .schedule(schedule)
                .build();

        given(jwtUtil.getUserInfoFromToken(accessToken)).willReturn(username);
        given(replyRepository.findById(any())).willReturn(Optional.of(reply));
        given(scheduleRepository.findById(any())).willReturn(Optional.of(schedule));

        //when
        replyService.deleteReply(accessToken, 1L, 1L);

        //then
        verify(replyRepository, times(1)).delete(reply);
    }
}