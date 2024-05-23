package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckGroupGetCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import static java.util.Objects.nonNull;

public class GroupGetCommandFactory implements UserCommandFactory {
    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return type == UserCommandType.GROUPS && !(nonNull(userInput.getName()) && nonNull(userInput.getUserId()));
    }

    @Override
    public CheckCommand<Output> createCommand(UserRepository repository, GroupRepository groupRepository,
                                              UserChecker userChecker, PasswordProtector passwordProtector,
                                              UserCommandType type, UserInput userInput) {
        return new CheckGroupGetCommand(repository, groupRepository, userChecker, passwordProtector,
                userInput.getName(), userInput.getUserId());
    }
}
