package com.michaelyi.recfoundry.gym;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GymQueueResponse {
    private String courtNumber;
    private String error;
}
