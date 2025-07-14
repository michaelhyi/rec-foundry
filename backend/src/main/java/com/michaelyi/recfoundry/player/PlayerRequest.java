package com.michaelyi.recfoundry.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequest {
    private String userId;
    private String bio;
    private String height;
    private String weight;
    private String position;
    private String yearsOfExperience;
    private String dateOfBirth;
}
