package com.sparta.todoapp.service;

import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.repository.port.MemberRepository;
import com.sparta.todoapp.service.port.UserService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
            () -> new NoSuchElementException("존재하지 않는 회원입니다.")
        );
    }

    public void validateDuplicateUsername(String username) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new DataIntegrityViolationException("중복된 사용자 이름이 존재합니다.");
        }
    }

    public void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new DataIntegrityViolationException("중복된 Email 입니다.");
        }
    }

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public String findById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
            () -> new NoSuchElementException("유저를 찾을 수 없습니다.")
        );

        return member.getUsername();
    }
}
