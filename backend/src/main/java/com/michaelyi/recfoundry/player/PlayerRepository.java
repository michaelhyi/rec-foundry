package com.michaelyi.recfoundry.player;

import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.actions.CreateRecFoundryPlayerActionRequest;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.actions.CreateRecFoundryPlayerActionResponse;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlayerRepository {
    @Autowired
    private FoundryClient client;

    public void createPlayer(Player player) {
        CreateRecFoundryPlayerActionResponse response = client.ontology()
                .actions()
                .createRecFoundryPlayer()
                .apply(CreateRecFoundryPlayerActionRequest.builder()
                        .height(player.getHeight())
                        .weight(player.getWeight())
                        .date_of_birth(player.getDateOfBirth())
                        .years_of_experience(player.getYearsOfExperience())
                        .bio(player.getBio())
                        .user_id(player.getUserId())
                        .position(player.getPosition())
                        .created_at(player.getCreatedAt())
                        .updated_at(player.getUpdatedAt())
                        .build());
    }

    public Optional<RecFoundryPlayer> getPlayerByUserId(String userId) {
        Optional<RecFoundryPlayer> result = client.ontology()
                .objects()
                .RecFoundryUser()
                .fetch(userId)
                .get()
                .recFoundryPlayer();
        return result;
    }
}
