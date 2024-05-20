package io.github.fherbreteau.functional.domain.command;

import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.Comparator;
import java.util.List;

public class CompositeUserCommandFactory {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserChecker userChecker;
    private final List<UserCommandFactory> factories;

    public CompositeUserCommandFactory(UserRepository userRepository, GroupRepository groupRepository,
                                       UserChecker userChecker, List<UserCommandFactory> factories) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userChecker = userChecker;
        this.factories = factories.stream().sorted(Comparator.comparing(UserCommandFactory::order)).toList();
    }

    public CheckCommand<Output> createCommand(UserCommandType type, UserInput userInput) {
        return factories.stream()
                .filter(f -> f.supports(type, userInput))
                .map(f -> f.createCommand(userRepository, groupRepository, userChecker, type, userInput))
                .findFirst()
                .orElseThrow();
    }

}
