package com.michaelyi.recfoundry.gym;

import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryGym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GymRepository {
    @Autowired
    private FoundryClient client;

    public List<RecFoundryGym> getAllGyms() {
        List<RecFoundryGym> result = client.ontology()
                .objects()
                .RecFoundryGym()
                .fetchStream()
                .toList();
        return result;
    }
}
