package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Schedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByIdAndUserId(Long id, Long user_id);

    Optional<List<Schedule>> findAllByUserIdOrderByIsCompletedAscModifiedAtDesc(Long id);
}
