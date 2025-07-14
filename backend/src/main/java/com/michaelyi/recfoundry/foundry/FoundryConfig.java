package com.michaelyi.recfoundry.foundry;

import com.palantir.osdk.api.Auth;
import com.palantir.osdk.api.UserTokenAuth;
import com.palantir.osdk.internal.api.FoundryConnectionConfig;
import com.palantirfoundry.usw17.michaelyi.rec_foundry_sdk.FoundryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoundryConfig {
    @Bean
    public FoundryClient foundryClient() {
        Auth auth = UserTokenAuth.builder()
                .token(System.getenv("FOUNDRY_TOKEN"))
                .build();

        FoundryClient client = FoundryClient.builder()
                .auth(auth)
                .connectionConfig(FoundryConnectionConfig.builder()
                        .foundryUri("https://michaelyi.usw-17.palantirfoundry.com")
                        .build())
                .build();
        return client;
    }
}
