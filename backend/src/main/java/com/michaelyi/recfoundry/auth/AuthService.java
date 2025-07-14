package com.michaelyi.recfoundry.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.michaelyi.recfoundry.user.User;
import com.michaelyi.recfoundry.user.UserRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public String login(String email, String password) {
        RecFoundryUser user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!BCrypt.verifyer().verify(password.toCharArray(), user.password().get()).verified) {
            log.warn("Login failed for user with email: {}", email);
            throw new IllegalArgumentException("Invalid password");
        }

        return user.id().get();
    }

    public String register(String email, String password, String firstName, String lastName) {
        log.info("Registering user with email: {}, firstName: {}, lastName: {}", email, firstName, lastName);
        User user = User.builder()
                .email(email)
                .password(BCrypt.withDefaults().hashToString(12, password.toCharArray()))
                .firstName(firstName)
                .lastName(lastName)
                .createdAt(String.valueOf(System.currentTimeMillis()))
                .updatedAt(String.valueOf(System.currentTimeMillis()))
                .build();

        userRepository.createUser(user);
        String userId = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found after registration"))
                .id()
                .get();

        log.info("User registered with ID: {}", userId);
        return userId;
    }
}
