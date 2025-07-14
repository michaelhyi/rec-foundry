package com.michaelyi.recfoundry.court;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelyi.recfoundry.player.Player;
import com.michaelyi.recfoundry.player.PlayerRepository;
import com.michaelyi.recfoundry.user.UserRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryCourt;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryPlayer;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryUser;
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

    @Autowired
    private UserRepository userRepository;

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

            log.debug("Fetching team data for court {}: teamOneJson={}, teamTwoJson={}",
                      court.getId(), teamOneJson, teamTwoJson);

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
            court.setQueueSize(getQueueSize(court.getId()));
            courts.add(court);
        }

        log.info("Returning {} courts for gym ID {}: {}", courts.size(), gymId, courts);
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
            log.debug("Processing player ID {} for court {}", playerId, courtId);
            Optional<RecFoundryPlayer> player = playerRepository.getPlayerById(playerId);
            if (player.isEmpty()) {
                log.warn("Player with ID {} not found in database for court {}", playerId, courtId);
                continue;
            }
            log.debug("Found player with ID {} in queue for court {}", playerId, courtId);

            Optional<RecFoundryUser> recFoundryUser = userRepository.getUserById(player.get().userId().get());
            if (recFoundryUser.isEmpty()) {
                log.warn("User with ID {} not found in database for player {}", player.get().userId().get(), playerId);
                continue;
            }

            Player p = Player.builder()
                    .id(player.get().id().get())
                    .userId(player.get().userId().get())
                    .firstName(recFoundryUser.get().firstName().get())
                    .lastName(recFoundryUser.get().lastName().get())
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

    public Long getQueueSize(String courtId) {
        Long size = redisTemplate.opsForList().size("court:" + courtId + ":queue");
        return size != null ? size : 0;
    }

    public void clearTeam(String courtId, int teamId) {
        if (teamId == 1) {
            redisTemplate.delete("court:" + courtId + ":teamOne");
        } else {
            redisTemplate.delete("court:" + courtId + ":teamTwo");
        }
    }

    public void nextTeam(String courtId, int teamId) throws JsonProcessingException {
        List<String> nextTeamPlayerIds = redisTemplate.opsForList().leftPop("court:" + courtId + ":queue", 5);
        List<Player> nextTeam = new ArrayList<>();

        for (String playerId : nextTeamPlayerIds) {
            Optional<RecFoundryPlayer> player = playerRepository.getPlayerById(playerId);
            if (player.isEmpty()) {
                log.warn("Player with ID {} not found in database for court {}", playerId, courtId);
                continue;
            }

            Optional<RecFoundryUser> recFoundryUser = userRepository.getUserById(player.get().userId().get());
            if (recFoundryUser.isEmpty()) {
                log.warn("User with ID {} not found in database for player {}", player.get().userId().get(), playerId);
                continue;
            }

            Player p = Player.builder()
                    .id(player.get().id().get())
                    .userId(player.get().userId().get())
                    .firstName(recFoundryUser.get().firstName().get())
                    .lastName(recFoundryUser.get().lastName().get())
                    .height(player.get().height().get())
                    .weight(player.get().weight().get())
                    .dateOfBirth(player.get().dateOfBirth().get())
                    .yearsOfExperience(player.get().yearsOfExperience().get())
                    .bio(player.get().bio().get())
                    .position(player.get().position().get())
                    .createdAt(player.get().createdAt().get())
                    .updatedAt(player.get().updatedAt().get())
                    .build();
            nextTeam.add(p);
        }

        if (teamId == 1) {
            redisTemplate.opsForValue().set("court:" + courtId + ":teamOne", jsonMapper.writeValueAsString(nextTeam));
        } else {
            redisTemplate.opsForValue().set("court:" + courtId + ":teamTwo", jsonMapper.writeValueAsString(nextTeam));
        }
    }
}
