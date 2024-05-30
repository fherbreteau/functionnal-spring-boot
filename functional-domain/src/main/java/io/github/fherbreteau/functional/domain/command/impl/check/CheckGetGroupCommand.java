package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.GetGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.driven.*;

import java.util.List;
import java.util.UUID;

public class CheckGetGroupCommand extends AbstractCheckGetInfoCommand<List<Group>, GetGroupCommand> {

    public CheckGetGroupCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker,
                                UserUpdater userUpdater, String name, UUID userId) {
        super(userRepository, groupRepository, userChecker, userUpdater, name, userId, UserCommandType.GROUPS);
    }

    @Override
    protected GetGroupCommand createSuccess() {
        return new GetGroupCommand(userRepository, groupRepository, name, userId);
    }
}
