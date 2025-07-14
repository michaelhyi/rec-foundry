package com.michaelyi.recfoundry.player;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/api/v1/players")
    public ResponseEntity<CreatePlayerResponse> createPlayer(@RequestBody PlayerRequest playerRequest) {
        try {
            String playerId = playerService.createPlayer(playerRequest);
            CreatePlayerResponse response = CreatePlayerResponse.builder()
                    .playerId(playerId)
                    .build();
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(201));
        } catch (NoSuchElementException e) {
            CreatePlayerResponse response = CreatePlayerResponse.builder()
                    .error("User not found.")
                    .build();
            return new ResponseEntity<>(response,HttpStatusCode.valueOf(404));
        } catch (Exception e) {
            CreatePlayerResponse response = CreatePlayerResponse.builder()
                    .error("An error occurred while creating the player.")
                    .build();
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping("/api/v1/users/{userId}/player")
    public ResponseEntity<Player> getPlayerByUserId(@PathVariable String userId) {
        try {
            Player player = playerService.getPlayerByUserId(userId);
            return new ResponseEntity<>(player, HttpStatusCode.valueOf(200));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }
}
