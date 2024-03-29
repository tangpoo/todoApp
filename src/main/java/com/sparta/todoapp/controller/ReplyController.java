package com.sparta.todoapp.controller;

import static com.sparta.todoapp.message.ReplyMessage.CREATE_REPLY_API;
import static com.sparta.todoapp.message.ReplyMessage.CREATE_REPLY_SUCCESS;
import static com.sparta.todoapp.message.ReplyMessage.DELETE_REPLY_API;
import static com.sparta.todoapp.message.ReplyMessage.PATCH_REPLY_API;
import static com.sparta.todoapp.message.ReplyMessage.PATCH_REPLY_SUCCESS;

import com.sparta.todoapp.dto.ResponseDto;
import com.sparta.todoapp.dto.reply.ReplyRequestDto;
import com.sparta.todoapp.dto.reply.ReplyResponseDto;
import com.sparta.todoapp.facade.ReplyFacade;
import com.sparta.todoapp.jwt.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ReplyController {

    private final ReplyFacade replyFacade;

    @PostMapping("/{scheduleId}/new")
    @Operation(summary = CREATE_REPLY_API)
    public ResponseEntity<ResponseDto<ReplyResponseDto>> createReply(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody ReplyRequestDto requestDto,
        @PathVariable Long scheduleId) {
        log.info(CREATE_REPLY_API);

        ReplyResponseDto responseDto = replyFacade.createReply(userDetails.getMember(), requestDto,
            scheduleId);

        return ResponseEntity.created(createUri(responseDto.getId()))
            .body(ResponseDto.<ReplyResponseDto>builder()
                .message(CREATE_REPLY_SUCCESS)
                .data(responseDto)
                .build());
    }

    @PatchMapping("/{scheduleId}/update/{replyId}")
    @Operation(summary = PATCH_REPLY_API)
    public ResponseEntity<ResponseDto<ReplyResponseDto>> updateReply(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody ReplyRequestDto requestDto,
        @PathVariable Long scheduleId,
        @PathVariable Long replyId
    ) {
        log.info(PATCH_REPLY_API);

        ReplyResponseDto responseDto = replyFacade.updateReply(userDetails.getMember(), requestDto,
            scheduleId,
            replyId);

        return ResponseEntity.created(updateUri())
            .body(ResponseDto.<ReplyResponseDto>builder()
                .message(PATCH_REPLY_SUCCESS)
                .data(responseDto)
                .build());
    }

    @DeleteMapping("/{scheduleId}/delete/{replyId}")
    @Operation(summary = DELETE_REPLY_API)
    public ResponseEntity<Void> deleteReply(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long scheduleId,
        @PathVariable Long replyId
    ) {
        log.info(DELETE_REPLY_API);

        replyFacade.deleteReply(userDetails.getMember(), scheduleId, replyId);

        return ResponseEntity.noContent().build();
    }

    private URI createUri(Long replyId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(replyId)
            .toUri();
    }

    private URI updateUri() {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .replaceQueryParam("isCompleted")
            .replaceQueryParam("isPrivate")
            .build()
            .toUri();
    }
}
