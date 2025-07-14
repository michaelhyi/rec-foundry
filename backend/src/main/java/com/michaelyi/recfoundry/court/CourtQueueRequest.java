package com.michaelyi.recfoundry.court;

import lombok.Data;

@Data
public class CourtQueueRequest {
    private String courtId;
    private String playerId;
}
