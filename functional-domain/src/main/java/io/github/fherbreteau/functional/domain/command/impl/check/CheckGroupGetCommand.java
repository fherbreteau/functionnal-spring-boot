package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.GroupGetCommand;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

public class CheckGroupGetCommand extends AbstractCheckGetInfoCommand<GroupGetCommand> {

    public CheckGroupGetCommand(UserRepository userRepository, GroupRepository groupRepository, UserChecker userChecker,
                                PasswordProtector passwordProtector, String name, UUID userId) {
        super(userRepository, groupRepository, userChecker, passwordProtector, name, userId, UserCommandType.GROUPS);
    }

    @Override
    protected GroupGetCommand createSuccess() {
        return new GroupGetCommand(userRepository, groupRepository, passwordProtector, name, userId);
    }
}
