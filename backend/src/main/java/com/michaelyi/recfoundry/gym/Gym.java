package com.michaelyi.recfoundry.gym;

import com.michaelyi.recfoundry.court.Court;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class Gym {
    private String id;
    private String name;
    private String address;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private List<Court> courts;
}
