package com.epam.gym.main.feign;

import com.epam.gym.main.dto.AuthUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "auth-service", path = "/api/v1/auth")
public interface AuthServiceClient {

    @PostMapping("/users")
    void createUser(@RequestBody AuthUserDTO createAuthUserDTO);

    @DeleteMapping("/users/{username}")
    void deleteUserByUsername(@PathVariable String username);

    @GetMapping("/users/exists/{username}")
    boolean isUsernameTaken(@PathVariable String username);
}