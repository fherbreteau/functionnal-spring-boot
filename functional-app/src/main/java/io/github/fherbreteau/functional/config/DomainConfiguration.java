package io.github.fherbreteau.functional.config;

import io.github.fherbreteau.functional.domain.access.CompositeAccessParserFactory;
import io.github.fherbreteau.functional.domain.access.factory.AccessParserFactory;
import io.github.fherbreteau.functional.domain.command.CompositeItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.CompositeUserCommandFactory;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.path.CompositePathFactory;
import io.github.fherbreteau.functional.domain.path.factory.PathFactory;
import io.github.fherbreteau.functional.domain.user.UserManager;
import io.github.fherbreteau.functional.driven.*;
import io.github.fherbreteau.functional.driving.AccessParserService;
import io.github.fherbreteau.functional.driving.FileService;
import io.github.fherbreteau.functional.driving.UserService;
import io.github.fherbreteau.functional.driving.impl.AccessParserServiceImpl;
import io.github.fherbreteau.functional.driving.impl.FileServiceImpl;
import io.github.fherbreteau.functional.driving.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DomainConfiguration {

    @Bean
    public FileService fileService(CompositeItemCommandFactory compositeItemCommandFactory,
                                   CompositePathFactory compositePathFactory) {
        return new FileServiceImpl(compositeItemCommandFactory, compositePathFactory);
    }

    @Bean
    public AccessParserService accessParserService(CompositeAccessParserFactory compositeAccessParserFactory) {
        return new AccessParserServiceImpl(compositeAccessParserFactory);
    }

    @Bean
    public UserService userService(UserManager userManager, CompositeUserCommandFactory compositeUserCommandFactory) {
        return new UserServiceImpl(userManager, compositeUserCommandFactory);
    }

    @Bean
    public UserManager userManager(UserRepository userRepository, GroupRepository groupRepository) {
        return new UserManager(userRepository, groupRepository);
    }

    @Bean
    public CompositeItemCommandFactory compositeCommandFactory(FileRepository fileRepository,
                                                               AccessChecker accessChecker,
                                                               ContentRepository contentRepository,
                                                               List<ItemCommandFactory> commandFactories) {
        return new CompositeItemCommandFactory(fileRepository, accessChecker, contentRepository, commandFactories);
    }

    @Bean
    public CompositePathFactory compositePathFactory(FileRepository fileRepository, AccessChecker accessChecker,
                                                     List<PathFactory> pathFactories) {
        CompositePathFactory factory = new CompositePathFactory(fileRepository, accessChecker, pathFactories);
        factory.configureRecursive();
        return factory;
    }

    @Bean
    public CompositeAccessParserFactory compositeAccessParserFactory(List<AccessParserFactory> accessParserFactories) {
        CompositeAccessParserFactory factory = new CompositeAccessParserFactory(accessParserFactories);
        factory.configureRecursive();
        return factory;
    }

    @Bean
    public CompositeUserCommandFactory compositeUserCommandFactory(UserRepository userRepository,
                                                                   GroupRepository groupRepository,
                                                                   UserChecker userChecker,
                                                                   PasswordProtector passwordProtector,
                                                                   List<UserCommandFactory> userFactories) {
        return new CompositeUserCommandFactory(userRepository, groupRepository, userChecker, passwordProtector,
                userFactories);
    }
}
