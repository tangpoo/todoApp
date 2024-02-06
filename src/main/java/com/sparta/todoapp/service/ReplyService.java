package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.reply.ReplyRequestDto;
import com.sparta.todoapp.dto.reply.ReplyResponseDto;
import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ReplyRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ReplyResponseDto createReply(String accessToken, ReplyRequestDto requestDto, Long id) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);

        User user = userRepository.findByUsername(author)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾지 못했습니다.")
                );
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일정을 찾지 못했습니다.")
                );

        Reply reply = new Reply(requestDto, schedule, user);

        return new ReplyResponseDto(replyRepository.save(reply));
    }

    @Transactional
    public ReplyResponseDto updateReply(String accessToken, ReplyRequestDto requestDto, Long scheduleId, Long replyId) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));

        if(!author.equals(reply.getUser().getUsername())) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        reply.update(requestDto);
        return new ReplyResponseDto(reply);
    }

    public void deleteReply(String accessToken, Long scheduleId, Long replyId) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));

        if(!author.equals(reply.getUser().getUsername())) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        replyRepository.delete(reply);
    }
}
