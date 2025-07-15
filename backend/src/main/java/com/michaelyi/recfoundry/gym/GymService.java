package com.michaelyi.recfoundry.gym;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.michaelyi.recfoundry.court.Court;
import com.michaelyi.recfoundry.court.CourtRepository;
import com.michaelyi.recfoundry.player.Player;
import com.michaelyi.recfoundry.player.PlayerRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryGym;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryPlayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class GymService {
    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Gym getGymById(String id) throws JsonProcessingException {
        Optional<RecFoundryGym> recFoundryGym = gymRepository.getGymById(id);

        if (recFoundryGym.isEmpty()) {
            throw new NoSuchElementException("Gym with ID " + id + " not found");
        }

        RecFoundryGym gym = recFoundryGym.get();
        List<Court> courts = courtRepository.getAllCourtsByGymId(gym.id().get());
        courts.sort(Comparator.comparing(Court::getCourtNumber));

        return Gym.builder()
                .id(gym.id().get())
                .name(gym.name().get())
                .address(gym.address().get())
                .createdAt(gym.createdAt().get())
                .updatedAt(gym.updatedAt().get())
                .courts(courts)
                .build();
    }

    public List<Gym> getAllGyms() {
        List<RecFoundryGym> recFoundryGyms = gymRepository.getAllGyms();
        List<Gym> gyms = new ArrayList<>();

        for (RecFoundryGym g : recFoundryGyms) {
            Gym gym = Gym.builder()
                    .id(g.id().get())
                    .name(g.name().get())
                    .address(g.address().get())
                    .createdAt(g.createdAt().get())
                    .updatedAt(g.updatedAt().get())
                    .build();
            gyms.add(gym);
        }

        return gyms;
    }

    public String joinShortestQueue(String gymId, String playerId) throws JsonProcessingException {
        Gym gym = getGymById(gymId);
        List<Court> courts = gym.getCourts();
        String smallestQueueCourtId = null;
        String smallestQueueCourtNum = null;
        Long smallestQueueSize = Long.MAX_VALUE;

        for (Court c : courts) {
            Long queueSize = c.getQueueSize();

            if (queueSize < smallestQueueSize) {
                smallestQueueCourtId = c.getId();
                smallestQueueSize = queueSize;
                smallestQueueCourtNum = c.getCourtNumber();
            }
        }

        courtRepository.queuePlayer(smallestQueueCourtId, playerId);
        return smallestQueueCourtNum;
    }

    public String joinMostBalancedQueue(String gymId, String playerId) throws JsonProcessingException {
        Gym gym = getGymById(gymId);
        Optional<RecFoundryPlayer> recFoundryPlayer = playerRepository.getPlayerById(playerId);
        if (recFoundryPlayer.isEmpty()) {
            throw new NoSuchElementException("Player with ID " + playerId + " not found");
        }

        RecFoundryPlayer player = recFoundryPlayer.get();
        String yearsOfExperience = player.yearsOfExperience().get();
        double minDifference = Double.MAX_VALUE;
        String mostBalancedCourtId = null;
        String mostBalancedCourtNum = null;

        List<Court> courts = gym.getCourts();
        for (Court c : courts) {
            int totalExperience = 0;

            for (Player p : c.getTeamOne()) {
                totalExperience += Integer.parseInt(p.getYearsOfExperience());
            }

            for (Player p : c.getTeamTwo()) {
                totalExperience += Integer.parseInt(p.getYearsOfExperience());
            }

            List<Player> queue = courtRepository.getQueue(c.getId());
            for (Player p : queue) {
                totalExperience += Integer.parseInt(p.getYearsOfExperience());
            }

            int numPlayers = c.getTeamOne().size() + c.getTeamTwo().size() + queue.size();
            double averageExperience = (double) totalExperience / numPlayers;
            double difference = Math.abs(averageExperience - Integer.parseInt(yearsOfExperience));

            if (difference < minDifference) {
                minDifference = difference;
                mostBalancedCourtId = c.getId();
                mostBalancedCourtNum = c.getCourtNumber();
            }
        }

        courtRepository.queuePlayer(mostBalancedCourtId, playerId);
        return mostBalancedCourtNum;
    }
}
