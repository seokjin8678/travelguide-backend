package com.seokjin.travelguide.repository.member;

import static com.seokjin.travelguide.domain.member.QMember.*;

import com.seokjin.travelguide.domain.member.Member;
import com.seokjin.travelguide.domain.member.QMember;
import com.seokjin.travelguide.repository.support.Querydsl5RepositorySupport;
import java.util.Optional;

public class MemberRepositoryImpl extends Querydsl5RepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Optional<String> findNicknameByEmail(String email) {
        return Optional.ofNullable(select(member.nickname)
                .from(member)
                .where(member.email.eq(email))
                .fetchOne());
    }
}
