package com.michaelyi.recfoundry.gym;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.michaelyi.recfoundry.court.Court;
import com.michaelyi.recfoundry.court.CourtRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryGym;
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
}
