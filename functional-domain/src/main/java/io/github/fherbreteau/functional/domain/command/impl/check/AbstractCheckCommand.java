package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public abstract class AbstractCheckCommand<C extends Command<Output>> implements Command<Command<Output>> {

    protected final FileRepository repository;

    protected final AccessChecker accessChecker;

    protected AbstractCheckCommand(FileRepository repository, AccessChecker accessChecker) {
        this.repository = repository;
        this.accessChecker = accessChecker;
    }

    @Override
    public final Command<Output> execute(User actor) {
        return checkAccess(actor) ? createSuccess() : createError();
    }

    protected abstract boolean checkAccess(User actor);

    protected abstract C createSuccess();

    protected ErrorCommand createError() {
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
