package io.github.fherbreteau.functional.config;

import com.authzed.api.v1.PermissionsServiceGrpc.PermissionsServiceBlockingStub;
import io.github.fherbreteau.functional.check.AccessCheckerImpl;
import io.github.fherbreteau.functional.check.UserCheckerImpl;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;
import io.github.fherbreteau.functional.update.AccessUpdaterImpl;
import io.github.fherbreteau.functional.update.UserUpdaterImpl;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@GrpcClientBean(
        clazz = PermissionsServiceBlockingStub.class,
        beanName = "permissionsService",
        client = @GrpcClient("spicedb"))
public class CheckConfiguration {

    @Bean
    AccessChecker accessChecker(PermissionsServiceBlockingStub permissionsService) {
        return new AccessCheckerImpl(permissionsService);
    }

    @Bean
    AccessUpdater accessUpdater(PermissionsServiceBlockingStub permissionsService) {
        return new AccessUpdaterImpl(permissionsService);
    }

    @Bean
    UserChecker userChecker(PermissionsServiceBlockingStub permissionsService) {
        return new UserCheckerImpl(permissionsService);
    }

    @Bean
    UserUpdater userUpdater(PermissionsServiceBlockingStub permissionsService) {
        return new UserUpdaterImpl(permissionsService);
    }
}
