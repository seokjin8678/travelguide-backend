package com.seokjin.travelguide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String username() default "test@test.com";
    String nickname() default "test";
    String password() default "123456";
    String[] roles() default { "MEMBER" };
}
