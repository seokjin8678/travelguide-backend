package com.seokjin.travelguide.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.seokjin.travelguide.domain.Member;
import com.seokjin.travelguide.dto.request.SignUpRequest;
import com.seokjin.travelguide.exception.InvalidRequestException;
import com.seokjin.travelguide.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    AuthService authService;

    @Spy
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
        when(memberRepository.existsByEmail("test@test.com"))
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
        when(memberRepository.existsByNickname("test"))
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
        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .build();
        doReturn(member).when(memberRepository).save(any(Member.class));

        // when
        Member signUpMember = authService.signUp(signUpRequest);

        // then
        assertThat(signUpMember.getEmail())
                .isEqualTo(signUpRequest.getEmail());
        assertThat(signUpMember.getNickname())
                .isEqualTo(signUpRequest.getNickname());
        assertThat(signUpMember.getPassword())
                .isNotEqualTo("1234");
    }
}