package com.seokjin.travelguide.repository.member;

import static org.assertj.core.api.Assertions.*;

import com.seokjin.travelguide.domain.member.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("데이터베이스에 회원 추가가 정상적으로 되어야 한다.")
    void saveMemberToDatabaseCouldBeSuccess() {
        // given
        Member member = member();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getEmail())
                .isEqualTo(member.getEmail());
        assertThat(savedMember.getPassword())
                .isEqualTo(member.getPassword());
        assertThat(savedMember.getNickname())
                .isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("데이터베이스에 회원 조회가 정상적으로 되어야 한다.")
    void findMembersToDatabaseCouldBeSuccess() {
        // given
        memberRepository.save(member());

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.size())
                .isEqualTo(1);
    }

    private Member member() {
        return Member.builder()
                .email("test@test.com")
                .password("123456")
                .nickname("test")
                .build();
    }
}