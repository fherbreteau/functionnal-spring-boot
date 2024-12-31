package io.github.fherbreteau.functional.domain.command.impl.check;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.command.impl.success.GetGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public class CheckGetGroupCommand extends AbstractCheckGetInfoCommand<List<Group>, GetGroupCommand> {

    public CheckGetGroupCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker,
                                UserUpdater userUpdater, String name, UUID userId) {
        super(userRepository, groupRepository, userChecker, userUpdater, name, userId, UserCommandType.GROUPS);
    }

    @Override
    protected GetGroupCommand createSuccess() {
        logger.log(DEBUG, "Creating execute command");
        return new GetGroupCommand(userRepository, groupRepository, name, userId);
    }
}
