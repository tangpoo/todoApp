package com.sparta.todoapp.service;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.todoapp.dto.schedule.ScheduleListResponseDto;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.QSchedule;
import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ReplyRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtil jwtUtil;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public ScheduleResponseDto createSchedule(String accessToken, ScheduleRequestDto requestDto) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();
        Schedule schedule = Schedule.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();

        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public ScheduleResponseDto getScheduleById(String accessToken, Long id) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);
        List<Reply> replies = replyRepository.findAllByScheduleId(schedule.getId());

        if (replies.size() > 0) {
            return new ScheduleResponseDto(schedule, getReplyList(replies));
        }

        return new ScheduleResponseDto(schedule);
    }

    private Map<Long, String> getReplyList(List<Reply> replies) {
        Map<Long, String> replyList = new LinkedHashMap<>();
        for (Reply reply : replies) {
            replyList.put(reply.getId(), reply.getContent());
        }

        return replyList;
    }

    public ScheduleListResponseDto getSchedules(String accessToken) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();;

        QSchedule qSchedule = new QSchedule("u");

        List<Schedule> schedules =
            queryFactory.selectFrom(qSchedule)
            .where(qSchedule.user.eq(user))
                .orderBy(qSchedule.isCompleted.asc(), qSchedule.createdAt.asc())
                .fetch();

        // 유저 객체를 가지고 schedule 을 isComplete 가 false 인 것, 작성일이 빠른 순으로 정렬
//        List<Schedule> schedules = scheduleRepository.findAllByUserIdOrderByIsCompletedAscModifiedAtDesc(user.getId()).orElseThrow();

        return new ScheduleListResponseDto(user.getUsername(), schedules.stream().map(ScheduleResponseDto::new).toList());
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(String accessToken, ScheduleRequestDto requestDto, Long id, boolean isCompleted, boolean isPrivate) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);

        if (isCompleted) {
            schedule.changeIsCompleted(isCompleted);
        }

        if (isPrivate) {
            schedule.changeIsPrivate(isPrivate);
        }

        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteSchedule(String accessToken, Long id) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);

        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void completedSchedule(String accessToken, Long id, boolean isCompleted, boolean isPrivate) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);

        if (isCompleted) {
            schedule.changeIsCompleted(isCompleted);
        }

        if (isPrivate) {
            schedule.changeIsPrivate(isPrivate);
        }

    }

    private Schedule getScheduleByTokenAndId(String accessToken, Long id) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();

        return scheduleRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));
    }
}
