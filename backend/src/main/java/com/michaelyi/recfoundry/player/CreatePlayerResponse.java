package com.michaelyi.recfoundry.player;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePlayerResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String playerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
}
