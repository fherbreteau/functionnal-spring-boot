package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.command.CompositeFactory;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.path.PathFactory;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import io.github.fherbreteau.functional.driving.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainConfiguration {

    @Bean
    public FileService fileService(CompositeFactory compositeFactory, PathFactory pathFactory) {
        return new FileService(compositeFactory, pathFactory);
    }

    @SuppressWarnings("rawtypes")
    @Bean
    public CompositeFactory compositeFactory(FileRepository fileRepository, AccessChecker accessChecker, List<CommandFactory> commandFactories) {
        return new CompositeFactory(fileRepository, accessChecker, commandFactories);
    }

    @Bean
    public PathFactory pathFactory(FileRepository fileRepository, AccessChecker accessChecker) {
        return new PathFactory(fileRepository, accessChecker);
    }
}
