package io.github.fherbreteau.functional.domain.command.factory.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUnsupportedUserCommand;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class UnsupportedUserCommandFactory implements UserCommandFactory<Void> {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return true;
    }

    @Override
    public CheckCommand<Void> createCommand(UserRepository repository, GroupRepository groupRepository,
                                            UserChecker userChecker, UserUpdater userUpdater,
                                            PasswordProtector passwordProtector, UserCommandType type,
                                            UserInput userInput) {
        debug(logger,  "Creating check command");
        return new CheckUnsupportedUserCommand(repository, groupRepository, userChecker, userUpdater, type,
                userInput);
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }
}
