package com.sparta.todoapp.repository;

import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByIdAndUserId(Long id, Long user_id);

    Optional<List<Schedule>> findAllByUserId(Long id);
}
