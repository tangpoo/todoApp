package com.sparta.todoapp.repository.port;

import com.sparta.todoapp.entity.ApiUseTime;
import com.sparta.todoapp.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
    Optional<ApiUseTime> findByMember(Member member);
}
