package com.michaelyi.recfoundry.court;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelyi.recfoundry.player.Player;
import com.michaelyi.recfoundry.player.PlayerRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryCourt;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryPlayer;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objectsets.RecFoundryCourtObjectSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CourtRepository {
    @Autowired
    private FoundryClient client;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private PlayerRepository playerRepository;

    public List<Court> getAllCourtsByGymId(String gymId) throws JsonProcessingException {
        log.info("Fetching all courts for gym with ID: {}", gymId);
        RecFoundryCourtObjectSet result = client.ontology()
                .objects()
                .RecFoundryGym()
                .fetch(gymId)
                .get()
                .recFoundryCourts();

        List<RecFoundryCourt> recFoundryCourts = result.fetchStream().toList();
        List<Court> courts = new ArrayList<>();

        for (RecFoundryCourt c : recFoundryCourts) {
            Court court = Court.builder()
                    .id(c.id().get())
                    .gymId(c.gymId().get())
                    .courtNumber(c.courtNumber().get())
                    .createdAt(c.createdAt().get())
                    .updatedAt(c.updatedAt().get())
                    .build();

            List<Player> teamOne;
            List<Player> teamTwo;

            String teamOneJson = redisTemplate.opsForValue()
                    .get("court:" + court.getId() + ":teamOne");
            String teamTwoJson = redisTemplate.opsForValue()
                    .get("court:" + court.getId() + ":teamTwe");

            if (teamOneJson == null) {
                teamOne = List.of();
            } else {
                teamOne = jsonMapper.readValue(teamOneJson, jsonMapper.getTypeFactory().constructCollectionType(List.class, Player.class));
            }

            if (teamTwoJson == null) {
                teamTwo = List.of();
            } else {
                teamTwo = jsonMapper.readValue(teamTwoJson, jsonMapper.getTypeFactory().constructCollectionType(List.class, Player.class));
            }

            court.setTeamOne(teamOne);
            court.setTeamTwo(teamTwo);
            courts.add(court);
        }

        return courts;
    }

    public void queuePlayer(String courtId, String playerId) {
        redisTemplate.opsForList().rightPush("court:" + courtId + ":queue", playerId);
    }

    public List<Player> getQueue(String courtId) {
        List<Player> queue = new ArrayList<>();
        List<String> playerIds = redisTemplate.opsForList().range("court:" + courtId + ":queue", 0, -1);
        log.debug("Fetched player IDs from queue for court {}: {}", courtId, playerIds);

        for (String playerId : playerIds) {
            Optional<RecFoundryPlayer> player = playerRepository.getPlayerById(playerId);
            if (player.isEmpty()) {
                continue;
            }
            log.debug("Found player with ID {} in queue for court {}", playerId, courtId);

            Player p = Player.builder()
                    .id(player.get().id().get())
                    .userId(player.get().userId().get())
                    .height(player.get().height().get())
                    .weight(player.get().weight().get())
                    .dateOfBirth(player.get().dateOfBirth().get())
                    .yearsOfExperience(player.get().yearsOfExperience().get())
                    .bio(player.get().bio().get())
                    .position(player.get().position().get())
                    .createdAt(player.get().createdAt().get())
                    .updatedAt(player.get().updatedAt().get())
                    .build();
            queue.add(p);
        }

        log.info("Returning queue for court {}: {}", courtId, queue);
        return queue;
    }
}
