package com.seokjin.travelguide.repository;

import com.seokjin.travelguide.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}