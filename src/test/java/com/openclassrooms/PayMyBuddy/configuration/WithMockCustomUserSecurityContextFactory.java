package com.openclassrooms.PayMyBuddy.configuration;

import com.openclassrooms.PayMyBuddy.model.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    public static final String TEST_USER_PASSWORD = "password";
    public final Long TEST_USER_ID = 1L;
    public final String TEST_USER_USERNAME = "testUser";

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetailsImpl principal = new UserDetailsImpl();
        principal.setId(TEST_USER_ID);
        principal.setUserName(TEST_USER_USERNAME);
        principal.setPassword(TEST_USER_PASSWORD);
        principal.setAuthorities(Collections.emptyList());

        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}