package io.github.fherbreteau.functional.domain.command;

import java.util.Comparator;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositeUserCommandFactory {
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserChecker userChecker;
    private final UserUpdater userUpdater;
    private final PasswordProtector passwordProtector;
    private final List<UserCommandFactory<?>> factories;

    public CompositeUserCommandFactory(UserRepository userRepository, GroupRepository groupRepository,
                                       UserChecker userChecker, UserUpdater userUpdater,
                                       PasswordProtector passwordProtector, List<UserCommandFactory<?>> factories) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userChecker = userChecker;
        this.userUpdater = userUpdater;
        this.passwordProtector = passwordProtector;
        this.factories = factories.stream().sorted(Comparator.comparing(UserCommandFactory::order)).toList();
    }

    @SuppressWarnings("unchecked")
    public <T> CheckCommand<T> createCommand(UserCommandType type, UserInput input) {
        logger.debug("Looking up for a command of type {}", type);
        return (CheckCommand<T>) factories.stream()
                .filter(f -> f.supports(type, input))
                .map(f -> f.createCommand(userRepository, groupRepository, userChecker, userUpdater, passwordProtector,
                        type, input))
                .findFirst()
                .orElseThrow();
    }

}
