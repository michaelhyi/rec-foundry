package com.michaelyi.recfoundry.user;

import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.actions.CreateRecFoundryUserActionRequest;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.actions.CreateRecFoundryUserActionResponse;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objects.RecFoundryUser;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objectsetqueries.RecFoundryUserFilter;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.objectsets.RecFoundryUserObjectSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepository {
    @Autowired
    private FoundryClient client;

    public void createUser(User user) {
        CreateRecFoundryUserActionResponse response = client.ontology()
                .actions()
                .createRecFoundryUser()
                .apply(CreateRecFoundryUserActionRequest.builder()
                        .first_name(user.getFirstName())
                        .last_name(user.getLastName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .created_at(user.getCreatedAt())
                        .updated_at(user.getUpdatedAt())
                        .build());

        log.debug("User created with response: {}", response);
    }

    public Optional<RecFoundryUser> getUserByEmail(String email) {
        RecFoundryUserObjectSet result = client.ontology()
                .objects()
                .RecFoundryUser()
                .where(RecFoundryUserFilter.email().eq(email));
        List<RecFoundryUser> recFoundryUsers = result.fetchStream()
                .toList();

        if (recFoundryUsers.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(recFoundryUsers.get(0));
    }
}
