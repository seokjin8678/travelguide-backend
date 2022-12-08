package com.seokjin.travelguide.service.auth;

import com.seokjin.travelguide.domain.member.Member;
import com.seokjin.travelguide.repository.member.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByEmail(username)
                .map(this::createUser)
                .orElseThrow(() -> {
                    log.info("{}에 대한 로그인이 실패 했습니다.", username);
                    throw new UsernameNotFoundException(username);
                });
    }

    private User createUser(Member member) {
        List<SimpleGrantedAuthority> authorities = member.getRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().name()))
                .collect(Collectors.toList());
        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
