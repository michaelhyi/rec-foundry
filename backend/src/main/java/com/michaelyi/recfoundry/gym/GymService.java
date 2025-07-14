package com.michaelyi.recfoundry.gym;

import com.michaelyi.recfoundry.court.Court;
import com.michaelyi.recfoundry.court.CourtRepository;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryCourt;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryGym;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GymService {
    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private CourtRepository courtRepository;

    public List<Gym> getAllGyms() {
        List<RecFoundryGym> recFoundryGyms = gymRepository.getAllGyms();
        List<Gym> gyms = new ArrayList<>();

        for (RecFoundryGym g : recFoundryGyms) {
            List<RecFoundryCourt> recFoundryCourts = courtRepository.getAllCourtsByGymId(g.id().get());
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

            Gym gym = Gym.builder()
                    .id(g.id().get())
                    .name(g.name().get())
                    .address(g.address().get())
                    .createdAt(g.createdAt().get())
                    .updatedAt(g.updatedAt().get())
                    .courts(courts)
                    .build();
            gyms.add(gym);
        }

        return gyms;
    }
}
