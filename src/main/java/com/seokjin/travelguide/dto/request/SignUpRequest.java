package com.seokjin.travelguide.dto.request;

import com.seokjin.travelguide.exception.InvalidRequestException;
import com.seokjin.travelguide.exception.Validation;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String nickname;

    public void validate() {
        if (!password.equals(confirmPassword)) {
            throw new InvalidRequestException(
                    "회원가입 오류: 비밀번호와 확인 비밀번호 불일치",
                    Validation.of("password", "비밀번호와 확인 비밀번호가 맞지 않습니다."),
                    Validation.of("confirmPassword", "비밀번호와 확인 비밀번호가 맞지 않습니다."));
        }
    }
}
