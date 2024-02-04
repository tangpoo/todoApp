package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ReplyRequestDto;
import com.sparta.todoapp.dto.ReplyResponseDto;
import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ReplyRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtil jwtUtil;

    public ReplyResponseDto createReply(String accessToken, ReplyRequestDto requestDto, Long id) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);

        User user = userRepository.findByUsername(author)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾지 못했습니다.")
                );
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("할일카드를 찾지 못했습니다.")
                );

        Reply reply = new Reply(requestDto, schedule, user);

        return new ReplyResponseDto(replyRepository.save(reply));
    }
}
