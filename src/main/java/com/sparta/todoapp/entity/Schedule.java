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
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 1024)
    private String content;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime date;

    @Column(name = "IS_COMPLETED")
    private boolean isCompleted;

    @Column(name = "IS_PRIVATE")
    private boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Schedule(ScheduleRequestDto requestDto, User user) {
        this.title = title;
        this.content = content;
        this.isCompleted = false;
        this.isPrivate = false;
        this.user = user;
    }

    public void update(ScheduleRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
