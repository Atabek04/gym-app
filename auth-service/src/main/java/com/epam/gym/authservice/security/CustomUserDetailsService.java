package com.epam.gym.authservice.security;

import com.epam.gym.authservice.repository.SecurityUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final SecurityUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        var authority = new SimpleGrantedAuthority(user.getRole().name());

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                user.isAccountNonLocked(),
                List.of(authority)
        );
    }
}
