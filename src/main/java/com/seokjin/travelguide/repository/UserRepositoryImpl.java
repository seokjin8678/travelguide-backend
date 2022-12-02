package com.seokjin.travelguide.repository;

import com.seokjin.travelguide.domain.User;
import com.seokjin.travelguide.repository.support.Querydsl5RepositorySupport;

public class UserRepositoryImpl extends Querydsl5RepositorySupport implements UserRepositoryCustom {

    public UserRepositoryImpl() {
        super(User.class);
    }
}
