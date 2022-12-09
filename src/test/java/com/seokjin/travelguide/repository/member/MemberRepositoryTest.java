package com.seokjin.travelguide.repository.member;

import static org.assertj.core.api.Assertions.*;

import com.seokjin.travelguide.domain.member.Member;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("데이터베이스에 회원이 정상적으로 저장되어야 한다.")
    void saveMemberToDatabaseCouldBeSuccess() {
        // given
        memberRepository.save(member());

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members.size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("데이터베이스에 회원이 정상적으로 조회되어야 한다.")
    void findMembersToDatabaseCouldBeSuccess() {
        // given
        Member member = member();

        // when
        memberRepository.save(member);
        em.clear();
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        assertThat(findMember.getEmail())
                .isEqualTo(member.getEmail());
        assertThat(findMember.getPassword())
                .isEqualTo(member.getPassword());
        assertThat(findMember.getNickname())
                .isEqualTo(member.getNickname());
    }

    private Member member() {
        return Member.builder()
                .email("test@test.com")
                .password("123456")
                .nickname("test")
                .build();
    }
}