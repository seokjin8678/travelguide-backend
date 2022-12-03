package com.seokjin.travelguide.service;

import com.seokjin.travelguide.domain.Member;
import com.seokjin.travelguide.repository.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByEmail(username)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private User createUser(Member member) {
        List<SimpleGrantedAuthority> authorities = member.getRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().name()))
                .collect(Collectors.toList());
        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
