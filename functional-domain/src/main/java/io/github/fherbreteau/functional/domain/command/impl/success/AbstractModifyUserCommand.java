package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public abstract class AbstractModifyUserCommand<T> extends AbstractSuccessUserCommand<T> {

    protected final UserUpdater userUpdater;

    protected AbstractModifyUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                                        UserUpdater userUpdater) {
        super(userRepository, groupRepository);
        this.userUpdater = userUpdater;
    }
}
