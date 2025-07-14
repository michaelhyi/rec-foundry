package com.michaelyi.recfoundry.gym;

import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryGym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GymRepository {
    @Autowired
    private FoundryClient client;

    public Optional<RecFoundryGym> getGymById(String id) {
        Optional<RecFoundryGym> result = client.ontology()
                .objects()
                .RecFoundryGym()
                .fetch(id);
        return result;
    }

    public List<RecFoundryGym> getAllGyms() {
        List<RecFoundryGym> result = client.ontology()
                .objects()
                .RecFoundryGym()
                .fetchStream()
                .toList();
        return result;
    }
}
