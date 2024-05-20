package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

public abstract class AbstractCheckUserCommand<C extends Command<Output>> implements CheckCommand<Output> {

    protected final UserRepository userRepository;
    protected final GroupRepository groupRepository;
    protected final UserChecker userChecker;

    protected AbstractCheckUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                                       UserChecker userChecker) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userChecker = userChecker;
    }

    @Override
    public final Command<Output> execute(User actor) {
        return checkAccess(actor) ? createSuccess() : createError();
    }

    protected abstract boolean checkAccess(User actor);

    protected abstract C createSuccess();

    protected UserErrorCommand createError() {
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
