package com.michaelyi.recfoundry.court;

import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryCourt;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objectsets.RecFoundryCourtObjectSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourtRepository {
    @Autowired
    private FoundryClient client;

    public List<RecFoundryCourt> getAllCourtsByGymId(String gymId) {
        RecFoundryCourtObjectSet result = client.ontology()
                .objects()
                .RecFoundryGym()
                .fetch(gymId)
                .get()
                .recFoundryCourts();

        return result.fetchStream().toList();
    }
}
