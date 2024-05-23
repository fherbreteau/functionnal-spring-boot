package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.GetUserCommand;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.driven.*;

import java.util.UUID;

public class CheckGetUserCommand extends AbstractCheckGetInfoCommand<GetUserCommand> {
    public CheckGetUserCommand(UserRepository repository, GroupRepository groupRepository, UserChecker userChecker,
                               UserUpdater userUpdater, String name, UUID userId) {
        super(repository, groupRepository, userChecker, userUpdater, name, userId, UserCommandType.ID);
    }

    @Override
    protected GetUserCommand createSuccess() {
        return new GetUserCommand(userRepository, groupRepository, name, userId);
    }
}
