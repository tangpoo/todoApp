package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule extends TimeStamped {

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    public void update(ScheduleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void changeIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void changeIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
