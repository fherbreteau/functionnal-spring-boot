package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUnsupportedUserCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.*;

public class UnsupportedUserCommandFactory implements UserCommandFactory<Void> {
    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return true;
    }

    @Override
    public CheckCommand<Void> createCommand(UserRepository repository, GroupRepository groupRepository,
                                              UserChecker userChecker, UserUpdater userUpdater,
                                              PasswordProtector passwordProtector, UserCommandType type,
                                              UserInput userInput) {
        return new CheckUnsupportedUserCommand(repository, groupRepository, userChecker, userUpdater, type,
                userInput);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
