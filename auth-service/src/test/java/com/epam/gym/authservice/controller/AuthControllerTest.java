package com.epam.gym.authservice.controller;

import com.epam.gym.authservice.dto.AuthUserDTO;
import com.epam.gym.authservice.dto.UserCredentials;
import com.epam.gym.authservice.dto.UserNewPasswordCredentials;
import com.epam.gym.authservice.model.UserRole;
import com.epam.gym.authservice.service.AuthService;
import com.epam.gym.authservice.service.SecurityUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityUserService securityUserService;

    @MockBean
    private AuthService authService;

    @Test
    void createUser_WithValidAuthUserDTO_ShouldReturnOk() throws Exception {
        var validUser = AuthUserDTO.builder()
                .username("Super.Man")
                .password("$2a$12W")
                .role(UserRole.ROLE_TRAINEE)
                .isActive(true)
                .build();

        mockMvc.perform(post("/api/v1/auth/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validUser)))
                .andExpect(status().isOk());

        verify(securityUserService, times(1)).createUser(validUser);
    }

    @Test
    void deleteUserByUsername_ShouldReturnOk() throws Exception {
        var username = "Super.Man";

        mockMvc.perform(delete("/api/v1/auth/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(securityUserService, times(1)).deleteUserByUsername(username);
    }

    @Test
    void isUsernameTaken_ShouldReturnTrue() throws Exception {
        var username = "Super.Man";
        when(securityUserService.isUsernameTaken(username)).thenReturn(true);

        mockMvc.perform(get("/api/v1/auth/users/exists/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(securityUserService, times(1)).isUsernameTaken(username);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnTokens() throws Exception {
        var credentials = new UserCredentials("Super.Trainer", "123");
        var tokens = Map.of("accessToken", "jwt-token", "refreshToken", "refresh-token");

        when(authService.login(credentials)).thenReturn(tokens);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));

        verify(authService, times(1)).login(credentials);
    }

    @Test
    void refreshToken_WithValidToken_ShouldReturnNewTokens() throws Exception {
        var refreshToken = "valid-refresh-token";
        var tokens = Map.of("accessToken", "new-jwt-token", "refreshToken", "new-refresh-token");

        when(authService.refreshToken(refreshToken)).thenReturn(tokens);

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

        verify(authService, times(1)).refreshToken(refreshToken);
    }

    @Test
    void changePassword_WithValidCredentials_ShouldReturnOk() throws Exception {
        var credentials = new UserNewPasswordCredentials("Super.Man", "oldPass", "newPass");

        mockMvc.perform(put("/api/v1/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(credentials)))
                .andExpect(status().isOk());

        verify(authService, times(1)).changePassword(credentials);
    }
}
