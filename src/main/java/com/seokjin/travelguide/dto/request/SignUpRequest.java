package com.seokjin.travelguide.dto.request;

import com.seokjin.travelguide.exception.InvalidRequestException;
import com.seokjin.travelguide.exception.Validation;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class SignUpRequest {

    @Email
    @NotBlank
    @Length(max = 30)
    private String email;

    @NotBlank
    @Length(min = 6, max = 20)
    private String password;

    @NotBlank
    @Length(min = 6, max = 20)
    private String confirmPassword;

    @NotBlank
    @Length(min = 2, max = 10)
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
