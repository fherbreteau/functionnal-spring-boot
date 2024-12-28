package io.github.fherbreteau.functional.config;

import com.authzed.grpcutil.BearerToken;
import io.grpc.CallCredentials;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SpiceDbProperties.class)
public class GrpcConfiguration {

    @Bean
    public CallCredentials bearerToken(SpiceDbProperties properties) {
        return new BearerToken(properties.getToken());
    }
}
