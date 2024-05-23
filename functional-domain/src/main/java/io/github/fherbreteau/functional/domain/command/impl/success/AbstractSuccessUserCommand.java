package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserRepository;

public abstract class AbstractSuccessUserCommand implements Command<Output> {

    protected final UserRepository userRepository;
    protected final GroupRepository groupRepository;
    protected final PasswordProtector passwordProtector;

    protected AbstractSuccessUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                                         PasswordProtector passwordProtector) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.passwordProtector = passwordProtector;
    }
}
