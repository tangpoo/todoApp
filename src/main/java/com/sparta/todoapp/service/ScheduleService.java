package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.ScheduleListResponseDto;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ScheduleResponseDto createSchedule(String accessToken, ScheduleRequestDto requestDto) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();
        Schedule schedule = new Schedule(requestDto, user);

        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public ScheduleResponseDto getScheduleById(Long id) {
        Schedule schedule = findSchedule(id);

        return new ScheduleResponseDto(schedule);
    }

    public List<ScheduleListResponseDto> getSchedules(String title) {
        List<User> users = userRepository.findAll();
        List<ScheduleListResponseDto> scheduleList = users.stream().map(ScheduleListResponseDto::new).toList();

        if(title == null){
            return scheduleList;
        }

        return getSchedulesByTitle(scheduleList, title);
    }

    private List<ScheduleListResponseDto> getSchedulesByTitle(List<ScheduleListResponseDto> scheduleList, String title) {
        return scheduleList.stream()
                .map(schedules -> {
                    List<ScheduleResponseDto> filteredSchedule = schedules.getSchedules().stream()
                            .filter(schedule -> schedule.getTitle().equals(title))
                            .toList();
                    return new ScheduleListResponseDto(schedules.getAuthor(), filteredSchedule);
                })
                .toList();
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(String accessToken, ScheduleRequestDto requestDto, Long id, boolean isCompleted, boolean isPrivate) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);

        if(isCompleted){
            schedule.changeIsCompleted(isCompleted);
        }

        if(isPrivate){
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

    private Schedule findSchedule(Long id){
        return scheduleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("일정이 존재하지 않습니다.")
        );
    }

    private Schedule getScheduleByTokenAndId(String accessToken, Long id) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();

        return getScheduleByAuthor(user, id);
    }

    private Schedule getScheduleByAuthor(User user, Long id) {
        return user.getSchedules().stream()
                .filter(schedule -> schedule.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("작성자만 삭제/수정할 수 있습니다.")
                );
    }
}
