package io.github.fherbreteau.functional.domain.command.impl.success;

import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;

public abstract class AbstractSuccessUserCommand<T> implements Command<Output<T>> {
    protected final Logger logger = Logger.getLogger(getClass().getSimpleName());

    protected final UserRepository userRepository;
    protected final GroupRepository groupRepository;

    protected AbstractSuccessUserCommand(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }
}
