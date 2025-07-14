package com.michaelyi.recfoundry.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest req) {
        try {
            String userId = authService.login(req.getEmail(), req.getPassword());
            AuthResponse response = AuthResponse.builder()
                    .userId(userId)
                    .build();
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        } catch (NoSuchElementException e) {
            log.error("user not found for email: {}", req.getEmail(), e);

            AuthResponse response = AuthResponse.builder()
                    .error("User not found")
                    .build();

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
        } catch (IllegalArgumentException e) {
            log.error("Invalid credentials for email: {}", req.getEmail(), e);

            AuthResponse response = AuthResponse.builder()
                    .error("Invalid credentials")
                    .build();

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
        } catch (Exception e) {
            log.error("Unexpected error during login for email: {}", req.getEmail(), e);

            AuthResponse response = AuthResponse.builder()
                    .error("An unexpected error occurred")
                    .build();

            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRegisterRequest req) {
        String userId = authService.register(req.getEmail(), req.getPassword(), req.getFirstName(), req.getLastName());
        log.debug("User registered with ID: {}", userId);

        AuthResponse response = AuthResponse.builder()
                .userId(userId)
                .build();

        log.info("User registration successful for userId: {}", userId);
        log.info("Returning response: {}", response);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
    }
}
