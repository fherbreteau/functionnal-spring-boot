package io.github.fherbreteau.functional.config;

import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.grpcutil.BearerToken;
import io.github.fherbreteau.functional.log.LogGrpcInterceptor;
import io.grpc.CallCredentials;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SpiceDbProperties.class)
@GrpcClientBean(
        clazz = PermissionsServiceGrpc.PermissionsServiceBlockingStub.class,
        beanName = "permissionService",
        client = @GrpcClient("spicedb")
)
public class SpiceDbConfiguration {

    @GrpcGlobalClientInterceptor
    LogGrpcInterceptor logClientInterceptor() {
        return new LogGrpcInterceptor();
    }

    @Bean
    public CallCredentials bearerToken(SpiceDbProperties properties) {
        return new BearerToken(properties.getAuthToken());
    }

}
