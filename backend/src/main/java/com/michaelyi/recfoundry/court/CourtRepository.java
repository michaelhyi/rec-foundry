package com.michaelyi.recfoundry.court;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaelyi.recfoundry.player.Player;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryCourt;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objectsets.RecFoundryCourtObjectSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class CourtRepository {
    @Autowired
    private FoundryClient client;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper jsonMapper;

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
}
