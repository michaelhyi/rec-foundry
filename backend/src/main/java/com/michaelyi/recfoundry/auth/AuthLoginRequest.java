package com.michaelyi.recfoundry.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class AuthLoginRequest {
    private String email;
    private String password;
}
