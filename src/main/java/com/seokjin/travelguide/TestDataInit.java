package com.seokjin.travelguide;

import com.seokjin.travelguide.domain.Member;
import com.seokjin.travelguide.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        Member admin = Member.builder()
                .email("admin@admin.com")
                .password(passwordEncoder.encode("123456"))
                .nickname("admin")
                .build();
        admin.setAdmin();

        Member member = Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("123456"))
                .nickname("member")
                .build();

        memberRepository.save(admin);
        memberRepository.save(member);
    }
}
