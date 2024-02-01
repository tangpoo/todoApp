package com.sparta.todoapp.security;

import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "UserDetailsServiceImpl")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("check");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("NOT Found" + username));

        log.info("user : " + user.getUsername());
        return new UserDetailsImpl(user);
    }

}
