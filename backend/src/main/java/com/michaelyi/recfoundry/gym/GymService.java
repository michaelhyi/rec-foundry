package com.michaelyi.recfoundry.gym;

import com.michaelyi.recfoundry.court.Court;
import com.michaelyi.recfoundry.court.CourtRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryCourt;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryGym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GymService {
    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private CourtRepository courtRepository;

    public Gym getGymById(String id) {
        Optional<RecFoundryGym> recFoundryGym = gymRepository.getGymById(id);

        if (recFoundryGym.isEmpty()) {
            throw new NoSuchElementException("Gym with ID " + id + " not found");
        }

        RecFoundryGym gym = recFoundryGym.get();
        List<RecFoundryCourt> recFoundryCourts = courtRepository.getAllCourtsByGymId(gym.id().get());
        List<Court> courts = new ArrayList<>();

        for (RecFoundryCourt c : recFoundryCourts) {
            Court court = Court.builder()
                    .id(c.id().get())
                    .gymId(c.gymId().get())
                    .courtNumber(c.courtNumber().get())
                    .createdAt(c.createdAt().get())
                    .updatedAt(c.updatedAt().get())
                    .build();
            courts.add(court);
        }

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
