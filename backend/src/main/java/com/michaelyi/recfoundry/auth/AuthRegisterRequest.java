package com.michaelyi.recfoundry.auth;

import lombok.Data;

@Data
public class AuthRegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
