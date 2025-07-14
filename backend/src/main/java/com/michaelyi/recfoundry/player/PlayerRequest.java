package com.michaelyi.recfoundry.player;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerRequest {
    private String userId;
    private String bio;
    private String height;
    private String weight;
    private String position;
    private String yearsOfExperience;
    private String dateOfBirth;
}
