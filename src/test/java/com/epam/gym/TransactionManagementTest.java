package com.epam.gym;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.profiles.active=dev"})
class TransactionManagementTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback(value = false)
    void testTransactionCommit() {
        jdbcTemplate.execute("INSERT INTO users_table (id, first_name, last_name, username, password, is_active, role) " +
                "VALUES (201, 'John', 'Doe', 'john.doe', 'password123', true, 'ROLE_TRAINEE')");

        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM users_table WHERE username = 'john.doe'");
        assertFalse(users.isEmpty());

        Map<String, Object> user = users.get(0);
        assertEquals("john.doe", user.get("username"));
        assertEquals("John", user.get("first_name"));
        assertEquals("Doe", user.get("last_name"));
    }

    @Test
    @Transactional
    @Rollback
    void testTransactionRollback() {
        jdbcTemplate.execute("INSERT INTO users_table (id, first_name, last_name, username, password, is_active, role) " +
                "VALUES (202, 'Jane', 'Doe', 'jane.doe', 'password456', true, 'ROLE_TRAINER')");

        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM users_table WHERE username = 'jane.doe'");
        assertFalse(users.isEmpty());

        Map<String, Object> user = users.get(0);
        assertEquals("jane.doe", user.get("username"));
        assertEquals("Jane", user.get("first_name"));
        assertEquals("Doe", user.get("last_name"));
    }

    @Test
    void testVerifyRollback() {
        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM users_table WHERE username = 'jane.doe'");
        assertTrue(users.isEmpty());
    }

    @Test
    void testVerifyCommit() {
        List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM users_table WHERE username = 'john.doe'");
        assertFalse(users.isEmpty());

        Map<String, Object> user = users.get(0);
        assertEquals("john.doe", user.get("username"));
        assertEquals("John", user.get("first_name"));
        assertEquals("Doe", user.get("last_name"));
    }
}
