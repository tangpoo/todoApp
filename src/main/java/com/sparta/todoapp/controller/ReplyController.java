package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ReplyRequestDto;
import com.sparta.todoapp.dto.ReplyResponseDto;
import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/schedule")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{scheduleId}/reply/new")
    public ResponseEntity<ResponseDto> createReply(
            @RequestHeader(value = "Authorization") String accessToken,
            @Valid @RequestBody ReplyRequestDto requestDto,
            @PathVariable Long scheduleId){
        log.info("댓글 작성");

        ReplyResponseDto responseDto = replyService.createReply(accessToken, requestDto, scheduleId);
        return ResponseEntity.ok().body(new ResponseDto("댓글 작성 성공", responseDto));
    }

    @PatchMapping("/{scheduleId}/reply/update/{replyId}")
    public ResponseEntity<ResponseDto> updateReply(
            @RequestHeader(value = "Authorization") String accessToken,
            @Valid @RequestBody ReplyRequestDto requestDto,
            @PathVariable Long scheduleId,
            @PathVariable Long replyId
    ){
        log.info("댓글 수정");

        ReplyResponseDto responseDto = replyService.updateReply(accessToken, requestDto, scheduleId, replyId);

        return ResponseEntity.ok().body(new ResponseDto("댓글 수정 성공", responseDto));
    }

    @DeleteMapping("/{scheduleId}/reply/delete/{replyId}")
    public ResponseEntity<ResponseDto> deleteReply(
            @RequestHeader(value = "Authorization") String accessToken,
            @PathVariable Long scheduleId,
            @PathVariable Long replyId
    ){
        log.info("댓글 삭제");

        replyService.deleteReply(accessToken, scheduleId, replyId);

        return ResponseEntity.ok().body(new ResponseDto("댓글 삭제 성공"));
    }
}
