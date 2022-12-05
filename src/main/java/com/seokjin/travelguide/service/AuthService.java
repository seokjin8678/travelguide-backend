package com.seokjin.travelguide.service;

import com.seokjin.travelguide.domain.Member;
import com.seokjin.travelguide.dto.request.SignUpRequest;
import com.seokjin.travelguide.exception.InvalidRequestException;
import com.seokjin.travelguide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Member signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("회원가입 오류: 이메일 중복", "email", "해당 이메일이 존재합니다.");
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new InvalidRequestException("회원가입 오류: 닉네임 중복", "nickname", "해당 닉네임이 존재합니다.");
        }
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();
        return memberRepository.save(member);
    }
}
