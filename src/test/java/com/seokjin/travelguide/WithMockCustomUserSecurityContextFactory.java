package com.seokjin.travelguide;

import com.seokjin.travelguide.service.auth.CustomUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role : customUser.roles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        CustomUser user = new CustomUser(customUser.username(), customUser.password(),
                customUser.nickname(),
                grantedAuthorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, customUser.password(), user.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
