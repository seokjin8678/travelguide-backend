package com.seokjin.travelguide.dto.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class SignInRequest {

    @NotBlank
    @Email
    @Length(max = 30)
    private String email;

    @NotBlank
    @Length(min = 6, max = 20)
    private String password;
}
