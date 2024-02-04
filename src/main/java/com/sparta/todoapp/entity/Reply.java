package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "REPLY")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String content;

    @Column
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Reply(ReplyRequestDto requestDto, Schedule schedule, User user) {
        this.content = requestDto.getContent();
        this.user = user;
        this.schedule = schedule;
    }

    public void update(ReplyRequestDto requestDto){
        this.content = requestDto.getContent();
    }
}
