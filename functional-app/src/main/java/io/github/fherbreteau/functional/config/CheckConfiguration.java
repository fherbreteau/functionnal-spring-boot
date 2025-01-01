package io.github.fherbreteau.functional.config;

import com.authzed.api.v1.PermissionsServiceGrpc.PermissionsServiceBlockingStub;
import com.authzed.api.v1.SchemaServiceGrpc.SchemaServiceBlockingStub;
import io.github.fherbreteau.functional.driven.rules.*;
import io.github.fherbreteau.functional.rules.check.AccessCheckerImpl;
import io.github.fherbreteau.functional.rules.check.UserCheckerImpl;
import io.github.fherbreteau.functional.rules.init.RuleLoaderImpl;
import io.github.fherbreteau.functional.rules.update.AccessUpdaterImpl;
import io.github.fherbreteau.functional.rules.update.UserUpdaterImpl;
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

    @Bean
    RuleLoader ruleLoader(@GrpcClient("spicedb") SchemaServiceBlockingStub schemaService) {
        return new RuleLoaderImpl(schemaService);
    }
}
