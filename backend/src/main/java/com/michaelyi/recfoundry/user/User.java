package com.michaelyi.recfoundry.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String createdAt;
    private String updatedAt;
}
