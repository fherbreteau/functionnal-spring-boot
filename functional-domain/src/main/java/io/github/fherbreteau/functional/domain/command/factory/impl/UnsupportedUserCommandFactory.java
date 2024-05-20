package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUnsupportedUserCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.*;

public class UnsupportedUserCommandFactory implements UserCommandFactory {
    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return true;
    }

    @Override
    public CheckCommand<Output> createCommand(UserRepository repository, GroupRepository groupRepository,
                                              UserChecker userChecker, UserCommandType type, UserInput userInput) {
        return new CheckUnsupportedUserCommand(repository, groupRepository, userChecker, type, userInput);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
