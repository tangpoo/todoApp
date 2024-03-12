package com.sparta.todoapp.dto.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyRequestDto {

    @NotBlank(message = "댓글 내용을 입력하세요.")
    @Size(max = 512)
    private String content;
}
