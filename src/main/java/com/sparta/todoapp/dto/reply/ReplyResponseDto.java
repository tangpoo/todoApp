package com.sparta.todoapp.dto.reply;

import com.sparta.todoapp.entity.Reply;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyResponseDto {

    private Long id;
    private String content;
    private LocalDateTime date;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.date = reply.getCreatedAt();
    }
}
