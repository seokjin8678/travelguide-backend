package com.seokjin.travelguide.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponse {
    private String email;
    private String nickname;

    @Builder
    public SignUpResponse(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
