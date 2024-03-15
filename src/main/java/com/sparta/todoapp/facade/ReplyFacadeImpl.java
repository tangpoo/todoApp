package com.sparta.todoapp.facade;

import com.sparta.todoapp.dto.reply.ReplyRequestDto;
import com.sparta.todoapp.dto.reply.ReplyResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.service.ReplyServiceImpl;
import com.sparta.todoapp.service.port.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyFacadeImpl implements ReplyFacade {

    private final ReplyServiceImpl replyServiceImpl;
    private final ScheduleService scheduleService;

    @Override
    public ReplyResponseDto createReply(Member member, ReplyRequestDto requestDto,
        Long scheduleId) {
        Schedule schedule = scheduleService.getScheduleByMemberIdAndId(
            member.getId(), scheduleId);

        Reply reply = Reply.builder()
            .content(requestDto.getContent())
            .member(member)
            .schedule(schedule)
            .build();

        return new ReplyResponseDto(replyServiceImpl.createReply(reply));
    }

    public ReplyResponseDto updateReply(Member member, ReplyRequestDto requestDto, Long scheduleId,
        Long replyId) {
        Schedule schedule = scheduleService.getScheduleByMemberIdAndId(
            member.getId(), scheduleId);

        Reply reply = replyServiceImpl.findById(replyId);

        if (!member.getId().equals(reply.getMember().getId())) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        reply.update(requestDto);
        return new ReplyResponseDto(reply);
    }

    @Override
    public void deleteReply(Member member, Long scheduleId, Long replyId) {
        Schedule schedule = scheduleService.getScheduleByMemberIdAndId(
            member.getId(), scheduleId);

        Reply reply = replyServiceImpl.findById(replyId);

        if (!member.getId().equals(reply.getMember().getId())) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        replyServiceImpl.deleteReply(reply);
    }
}
