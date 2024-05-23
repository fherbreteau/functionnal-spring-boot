package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUserAddCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import static java.util.Objects.nonNull;

public class UserAddCommandFactory implements UserCommandFactory {
    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return type == UserCommandType.USERADD && isValid(userInput);
    }

    private boolean isValid(UserInput userInput) {
        return nonNull(userInput.getName());
    }

    @Override
    public CheckCommand<Output> createCommand(UserRepository repository, GroupRepository groupRepository,
                                              UserChecker userChecker, PasswordProtector passwordProtector,
                                              UserCommandType type, UserInput userInput) {
        return new CheckUserAddCommand(repository, groupRepository, userChecker, passwordProtector, userInput);
    }
}
