package com.epam.gym.security;

import com.epam.gym.exception.ForbiddenException;
import com.epam.gym.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {
    private final AuthenticationService authService;

    @Before("@annotation(secured)")
    public void authAndCheckSecurity(Secured secured) {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");

        authService.authenticate(authHeader);

        AuthenticatedUser authenticatedUser = AuthenticationContext.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new SecurityException("No authenticated user found");
        }

        UserRole[] allowedRoles = secured.value();
        boolean authorized = Arrays.stream(allowedRoles)
                .anyMatch(role -> role.equals(authenticatedUser.getRole()));

        if (!authorized) {
            throw new ForbiddenException("User does not have the required authority to access this method");
        }
    }

    @After("@annotation(Secured)")
    public void clearAuthenticationContext() {
        AuthenticationContext.clear();
    }
}
