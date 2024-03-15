package com.sparta.todoapp.facade;

import com.sparta.todoapp.dto.reply.ReplyRequestDto;
import com.sparta.todoapp.dto.reply.ReplyResponseDto;
import com.sparta.todoapp.entity.Member;

public interface ReplyFacade {

    ReplyResponseDto createReply(Member member, ReplyRequestDto requestDto, Long id);

    ReplyResponseDto updateReply(Member member, ReplyRequestDto requestDto,
        Long scheduleId, Long replyId);

    void deleteReply(Member member, Long scheduleId, Long replyId);
}
