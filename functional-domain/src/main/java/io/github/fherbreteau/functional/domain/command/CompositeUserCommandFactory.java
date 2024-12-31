package io.github.fherbreteau.functional.domain.command;

import static java.lang.System.Logger.Level.DEBUG;

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

public class CompositeUserCommandFactory {
    private final System.Logger logger = System.getLogger("CompositeUserCommandFactory");

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

    @SuppressWarnings("rawtypes")
    public CheckCommand createCommand(UserCommandType type, UserInput input) {
        logger.log(DEBUG, "Looking up for a command of type {0} on {1}", type, input);
        return factories.stream()
                .filter(f -> f.supports(type, input))
                .map(f -> f.createCommand(userRepository, groupRepository, userChecker, userUpdater, passwordProtector,
                        type, input))
                .findFirst()
                .orElseThrow();
    }

}
