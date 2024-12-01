package com.epam.gym.util;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.List;

@UtilityClass
public class UserUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final int PASSWORD_LENGTH = 10;
    private static final int INITIAL_COUNTER_VALUE = 1;

    public static String generateUsername(String firstName, String lastName, List<String> existingUsernames) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = INITIAL_COUNTER_VALUE;
        while (existingUsernames.contains(username)) {
            username = baseUsername + ++counter;
        }
        return username;
    }


    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
}