package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.UserGetCommand;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.UUID;

public class CheckUserGetCommand extends AbstractCheckGetInfoCommand<UserGetCommand> {
    public CheckUserGetCommand(UserRepository repository, GroupRepository groupRepository, UserChecker userChecker,
                               PasswordProtector passwordProtector, String name, UUID userId) {
        super(repository, groupRepository, userChecker, passwordProtector, name, userId, UserCommandType.ID);
    }

    @Override
    protected UserGetCommand createSuccess() {
        return new UserGetCommand(userRepository, groupRepository, passwordProtector, name, userId);
    }
}
