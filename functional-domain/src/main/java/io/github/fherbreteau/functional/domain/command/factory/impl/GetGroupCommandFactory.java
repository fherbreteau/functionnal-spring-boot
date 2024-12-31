package io.github.fherbreteau.functional.domain.command.factory.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;
import static java.util.Objects.nonNull;

import java.util.List;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.UserCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckGetGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class GetGroupCommandFactory implements UserCommandFactory<List<Group>> {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(UserCommandType type, UserInput userInput) {
        return type == UserCommandType.GROUPS && !(nonNull(userInput.getName()) && nonNull(userInput.getUserId()));
    }

    @Override
    public CheckCommand<List<Group>> createCommand(UserRepository repository, GroupRepository groupRepository,
                                                   UserChecker userChecker, UserUpdater userUpdater,
                                                   PasswordProtector passwordProtector, UserCommandType type,
                                                   UserInput userInput) {
        debug(logger,  "Creating check command");
        return new CheckGetGroupCommand(repository, groupRepository, userChecker, userUpdater,
                userInput.getName(), userInput.getUserId());
    }
}
