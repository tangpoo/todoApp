package com.sparta.todoapp.repository.port;

import com.sparta.todoapp.entity.Schedule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByIdAndMemberId(Long id, Long user_id);

//    Optional<List<Schedule>> findAllByMemberIdOrderByIsCompletedAscModifiedAtDesc(Long id);
}
