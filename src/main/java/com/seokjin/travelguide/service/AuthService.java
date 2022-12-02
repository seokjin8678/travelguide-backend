package com.seokjin.travelguide.service;

import com.seokjin.travelguide.domain.User;
import com.seokjin.travelguide.dto.request.SignUpRequest;
import com.seokjin.travelguide.dto.response.SignUpResponse;
import com.seokjin.travelguide.exception.InvalidRequestException;
import com.seokjin.travelguide.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public SignUpResponse signup(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("email", "해당 이메일이 존재합니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new InvalidRequestException("nickname", "해당 닉네임이 존재합니다.");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);

        return SignUpResponse.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .build();
    }
}
