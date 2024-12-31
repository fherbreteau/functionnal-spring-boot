package io.github.fherbreteau.functional.domain.command.impl.check;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.command.impl.success.GetUserCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class CheckGetUserCommand extends AbstractCheckGetInfoCommand<User, GetUserCommand> {
    public CheckGetUserCommand(UserRepository repository, GroupRepository groupRepository, UserChecker userChecker,
                               UserUpdater userUpdater, String name, UUID userId) {
        super(repository, groupRepository, userChecker, userUpdater, name, userId, UserCommandType.ID);
    }

    @Override
    protected GetUserCommand createSuccess() {
        logger.log(DEBUG, "Creating execute command");
        return new GetUserCommand(userRepository, groupRepository, name, userId);
    }
}
