package com.seokjin.travelguide.service.member;

import com.seokjin.travelguide.exception.MemberNotFoundException;
import com.seokjin.travelguide.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public String findNicknameByEmail(String email) {
        return memberRepository.findNicknameByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("회원 조회 오류: 존재하지 않는 이메일"));
    }
}
