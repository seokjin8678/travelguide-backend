package com.seokjin.travelguide.repository.member;

import com.seokjin.travelguide.domain.member.Member;
import com.seokjin.travelguide.repository.support.Querydsl5RepositorySupport;

public class MemberRepositoryImpl extends Querydsl5RepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }
}
