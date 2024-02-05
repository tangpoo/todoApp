package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 1024)
    private String content;

    @Column(name = "IS_COMPLETED")
    private boolean isCompleted;

    @Column(name = "IS_PRIVATE")
    private boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Schedule(ScheduleRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.isCompleted = false;
        this.isPrivate = false;
        this.user = user;
    }

    public void update(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void changeIsCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
    }

    public void changeIsPrivate(boolean isPrivate){
        this.isPrivate = isPrivate;
    }
}
