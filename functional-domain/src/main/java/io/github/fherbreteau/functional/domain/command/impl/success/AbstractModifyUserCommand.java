package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserRepository;
import io.github.fherbreteau.functional.driven.UserUpdater;

public abstract class AbstractModifyUserCommand extends AbstractSuccessUserCommand {

    protected final UserUpdater userUpdater;

    protected AbstractModifyUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                                        UserUpdater userUpdater) {
        super(userRepository, groupRepository);
        this.userUpdater = userUpdater;
    }
}
