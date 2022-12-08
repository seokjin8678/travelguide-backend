package com.seokjin.travelguide.repository.member;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<String> findNicknameByEmail(String email);
}
