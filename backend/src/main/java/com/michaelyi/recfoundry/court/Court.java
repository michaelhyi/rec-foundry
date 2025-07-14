package com.michaelyi.recfoundry.court;

import com.michaelyi.recfoundry.player.Player;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Court {
    private String id;
    private String gymId;
    private String courtNumber;
    private String createdAt;
    private String updatedAt;

    private List<Player> teamOne;
    private List<Player> teamTwo;

    private Long queueSize;
}
