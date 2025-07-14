package com.michaelyi.recfoundry.court;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Court {
    private String id;
    private String gymId;
    private String courtNumber;
    private String createdAt;
    private String updatedAt;
}
