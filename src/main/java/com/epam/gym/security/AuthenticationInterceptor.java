package com.epam.gym.security;

import com.epam.gym.actuator.RequestMetricsService;
import com.epam.gym.exception.AuthenticationException;
import com.epam.gym.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationService authService;
    private final RequestMetricsService requestMetricsService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        requestMetricsService.incrementRequestCounter();
        try {
            authService.authenticate(authHeader);
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            return false;
        }

        return true;
    }
}
