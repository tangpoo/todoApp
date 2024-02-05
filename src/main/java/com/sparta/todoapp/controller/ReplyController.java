package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.ReplyRequestDto;
import com.sparta.todoapp.dto.ReplyResponseDto;
import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.sparta.todoapp.message.ReplyMessage.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/schedule")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{scheduleId}/reply/new")
    @Operation(summary = CREATE_REPLY_API)
    public ResponseEntity<ResponseDto<ReplyResponseDto>> createReply(
            @RequestHeader(value = "Authorization") String accessToken,
            @Valid @RequestBody ReplyRequestDto requestDto,
            @PathVariable Long scheduleId){
        log.info(CREATE_REPLY_API);

        ReplyResponseDto responseDto = replyService.createReply(accessToken, requestDto, scheduleId);

        return ResponseEntity.created(createUri(responseDto.getId()))
                .body(ResponseDto.<ReplyResponseDto>builder()
                        .message(CREATE_REPLY_SUCCESS)
                        .data(responseDto)
                        .build());
    }

    @PatchMapping("/{scheduleId}/reply/update/{replyId}")
    @Operation(summary = PATCH_REPLY_API)
    public ResponseEntity<ResponseDto<ReplyResponseDto>> updateReply(
            @RequestHeader(value = "Authorization") String accessToken,
            @Valid @RequestBody ReplyRequestDto requestDto,
            @PathVariable Long scheduleId,
            @PathVariable Long replyId
    ){
        log.info(PATCH_REPLY_API);

        ReplyResponseDto responseDto = replyService.updateReply(accessToken, requestDto, scheduleId, replyId);

        return ResponseEntity.created(updateUri())
                .body(ResponseDto.<ReplyResponseDto>builder()
                        .message(PATCH_REPLY_SUCCESS)
                        .data(responseDto)
                        .build());
    }

    @DeleteMapping("/{scheduleId}/reply/delete/{replyId}")
    @Operation(summary = DELETE_REPLY_API)
    public ResponseEntity<Void> deleteReply(
            @RequestHeader(value = "Authorization") String accessToken,
            @PathVariable Long scheduleId,
            @PathVariable Long replyId
    ){
        log.info(DELETE_REPLY_API);

        replyService.deleteReply(accessToken, scheduleId, replyId);

        return ResponseEntity.noContent().build();
    }

    private URI createUri(Long replyId){
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(replyId)
                .toUri();
    }

    private URI updateUri(){
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam("isCompleted")
                .replaceQueryParam("isPrivate")
                .build()
                .toUri();
    }
}
