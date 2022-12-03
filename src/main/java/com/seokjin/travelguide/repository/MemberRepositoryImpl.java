package com.seokjin.travelguide.repository;

import com.seokjin.travelguide.domain.Member;
import com.seokjin.travelguide.repository.support.Querydsl5RepositorySupport;

public class MemberRepositoryImpl extends Querydsl5RepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }
}
