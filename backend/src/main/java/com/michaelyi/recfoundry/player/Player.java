package com.michaelyi.recfoundry.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String bio;
    private String height;
    private String weight;
    private String position;
    private String yearsOfExperience;
    private String dateOfBirth;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
