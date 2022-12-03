package com.seokjin.travelguide.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.seokjin.travelguide.domain.User;
import com.seokjin.travelguide.dto.request.SignUpRequest;
import com.seokjin.travelguide.exception.InvalidRequestException;
import com.seokjin.travelguide.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository);
    }

    @Test
    @DisplayName("회원가입 시 동일한 이메일이 있으면 예외가 발생해야 한다.")
    void signUpByExistsEmailCouldBeThrowException() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setNickname("test");
        signUpRequest.setPassword("1234");
        signUpRequest.setConfirmPassword("1234");

        // when
        when(userRepository.existsByEmail("test@test.com"))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> authService.signUp(signUpRequest))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    @DisplayName("회원가입 시 동일한 닉네임이 있으면 예외가 발생해야 한다.")
    void signUpByExistsNicknameCouldBeThrowException() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setNickname("test");
        signUpRequest.setPassword("1234");
        signUpRequest.setConfirmPassword("1234");

        // when
        when(userRepository.existsByNickname("test"))
                .thenReturn(true);

        // then
        assertThatThrownBy(() -> authService.signUp(signUpRequest))
                .isInstanceOf(InvalidRequestException.class);
    }
    
    @Test
    @DisplayName("회원가입이 정상적으로 되어야 한다.")
    void signUpByNoExistsEmailAndNicknameCouldBeSuccess() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("abc@test.com");
        signUpRequest.setNickname("aaa");
        signUpRequest.setPassword("1234");
        signUpRequest.setConfirmPassword("1234");
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .nickname(signUpRequest.getNickname())
                .build();
        doReturn(user).when(userRepository).save(any(User.class));

        // when
        User signUpUser = authService.signUp(signUpRequest);

        // then
        assertThat(signUpUser.getEmail())
                .isEqualTo(signUpRequest.getEmail());
        assertThat(signUpUser.getNickname())
                .isEqualTo(signUpRequest.getNickname());
    }
}