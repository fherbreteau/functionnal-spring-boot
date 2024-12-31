package io.github.fherbreteau.functional.config;

import com.authzed.grpcutil.BearerToken;
import io.grpc.CallCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {

    @Bean
    public CallCredentials bearerToken(@Value("${grpc.client.spicedb.token}") String token) {
        return new BearerToken(token);
    }
}
