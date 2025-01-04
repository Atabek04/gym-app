package com.epam.gym.authservice;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "spring.profiles.active=test")
class DatabaseInitializationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDataInitialization() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM security_user", Integer.class);
        assertThat(count).isEqualTo(3); // Ensure 3 records are inserted
    }
}
