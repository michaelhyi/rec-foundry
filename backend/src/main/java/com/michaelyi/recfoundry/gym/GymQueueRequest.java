package com.michaelyi.recfoundry.gym;

import lombok.Data;

@Data
public class GymQueueRequest {
    private String gymId;
    private String playerId;
    private GymQueueStrategy strategy;
}
