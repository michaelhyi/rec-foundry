package com.michaelyi.recfoundry.player;

import com.michaelyi.recfoundry.user.UserRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryPlayer;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    public String createPlayer(PlayerRequest req) {
        Player player = Player.builder()
                .userId(req.getUserId())
                .position(req.getPosition())
                .height(req.getHeight())
                .weight(req.getWeight())
                .dateOfBirth(req.getDateOfBirth())
                .yearsOfExperience(req.getYearsOfExperience())
                .bio(req.getBio())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        playerRepository.createPlayer(player);

        Optional<RecFoundryPlayer> createdPlayer = playerRepository.getPlayerByUserId(player.getUserId());
        if (createdPlayer.isEmpty()) {
            throw new NoSuchElementException("Player not found after creation for user ID: " + player.getUserId());
        }

        return createdPlayer.get().id().get();
    }

    public Player getPlayerByUserId(String userId) {
        RecFoundryPlayer recFoundryPlayer = playerRepository.getPlayerByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("Player not found for user ID: " + userId));

        RecFoundryUser recFoundryUser = userRepository.getUserById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found for user ID: " + userId));

        return Player.builder()
                .id(recFoundryPlayer.id().get())
                .userId(recFoundryPlayer.userId().get())
                .firstName(recFoundryUser.firstName().get())
                .lastName(recFoundryUser.lastName().get())
                .position(recFoundryPlayer.position().get())
                .height(recFoundryPlayer.height().get())
                .weight(recFoundryPlayer.weight().get())
                .dateOfBirth(recFoundryPlayer.dateOfBirth().get())
                .yearsOfExperience(recFoundryPlayer.yearsOfExperience().get())
                .bio(recFoundryPlayer.bio().orElse(null))
                .createdAt(recFoundryPlayer.createdAt().get())
                .updatedAt(recFoundryPlayer.updatedAt().get())
                .build();
    }
}
