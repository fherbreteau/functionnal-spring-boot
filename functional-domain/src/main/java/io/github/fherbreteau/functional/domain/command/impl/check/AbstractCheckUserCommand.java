package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.UserErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.driven.PasswordProtector;
import io.github.fherbreteau.functional.driven.UserChecker;
import io.github.fherbreteau.functional.driven.UserRepository;

import java.util.List;

public abstract class AbstractCheckUserCommand<C extends Command<Output>> implements CheckCommand<Output> {

    protected final UserRepository userRepository;
    protected final GroupRepository groupRepository;
    protected final UserChecker userChecker;
    protected final PasswordProtector passwordProtector;

    protected AbstractCheckUserCommand(UserRepository userRepository, GroupRepository groupRepository,
                                       UserChecker userChecker, PasswordProtector passwordProtector) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userChecker = userChecker;
        this.passwordProtector = passwordProtector;
    }

    @Override
    public final Command<Output> execute(User actor) {
        List<String> reasons = checkAccess(actor);
        if (!reasons.isEmpty()) {
            return createError(reasons);
        }
        return createSuccess();
    }

    protected abstract List<String> checkAccess(User actor);

    protected abstract C createSuccess();

    protected UserErrorCommand createError(List<String> reasons) {
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
