package com.epam.gym.authservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "security_user_seq")
    @SequenceGenerator(name = "security_user_seq", sequenceName = "security_user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "failed_login_count", columnDefinition = "integer default 0")
    private int failedLoginAttempts;

    @Column(name = "is_account_non_locked", columnDefinition = "boolean default true")
    private boolean isAccountNonLocked = true;

    @Column(name = "lockout_time")
    private LocalDateTime lockoutTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
}
