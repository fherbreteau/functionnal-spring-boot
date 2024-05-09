package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.command.CompositeCommandFactory;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.path.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;
import io.github.fherbreteau.functional.driving.FileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainConfiguration {

    @Bean
    public FileService fileService(CompositeCommandFactory compositeCommandFactory, CompositePathFactory compositePathFactory) {
        return new FileService(compositeCommandFactory, compositePathFactory);
    }

    @Bean
    public CompositeCommandFactory compositeCommandFactory(FileRepository fileRepository, AccessChecker accessChecker, List<CommandFactory> commandFactories) {
        return new CompositeCommandFactory(fileRepository, accessChecker, commandFactories);
    }

    @Bean
    public CompositePathFactory compositePathFactory(FileRepository fileRepository, AccessChecker accessChecker, List<PathFactory> pathFactories) {
        CompositePathFactory factory = new CompositePathFactory(fileRepository, accessChecker, pathFactories);
        factory.configureRecursives();
        return factory;
    }
}
