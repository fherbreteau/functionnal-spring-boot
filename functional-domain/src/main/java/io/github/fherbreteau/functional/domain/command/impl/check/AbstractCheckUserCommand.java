package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.driven.rules.UserUpdater;

import java.util.List;

public abstract class AbstractCheckUserCommand<T, C extends Command<Output<T>>> implements CheckCommand<T> {

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

    @Override
    public final Command<Output<T>> execute(User actor) {
        List<String> reasons = checkAccess(actor);
        if (!reasons.isEmpty()) {
            return createError(reasons);
        }
        return createSuccess();
    }

    protected abstract List<String> checkAccess(User actor);

    protected abstract C createSuccess();

    protected UserErrorCommand<T> createError(List<String> reasons) {
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
