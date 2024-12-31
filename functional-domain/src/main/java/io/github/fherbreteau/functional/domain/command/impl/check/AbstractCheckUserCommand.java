package io.github.fherbreteau.functional.domain.command.impl.check;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

public abstract class AbstractCheckUserCommand<T, C extends Command<Output<T>>> extends AbstractCheckCommand<T, C, UserErrorCommand<T>> {
    protected final UserRepository userRepository;
    protected final GroupRepository groupRepository;
    protected final UserChecker userChecker;
    protected final UserUpdater userUpdater;

    protected AbstractCheckUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                                       UserChecker userChecker, UserUpdater userUpdater) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userChecker = userChecker;
        this.userUpdater = userUpdater;
    }

    protected UserErrorCommand<T> createError(List<String> reasons) {
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
