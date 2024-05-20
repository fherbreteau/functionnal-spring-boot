package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public abstract class AbstractCheckItemCommand<C extends Command<Output>> implements CheckCommand<Output> {

    protected final FileRepository repository;

    protected final AccessChecker accessChecker;

    protected AbstractCheckItemCommand(FileRepository repository, AccessChecker accessChecker) {
        this.repository = repository;
        this.accessChecker = accessChecker;
    }

    @Override
    public final Command<Output> execute(User actor) {
        return checkAccess(actor) ? createSuccess() : createError();
    }

    protected abstract boolean checkAccess(User actor);

    protected abstract C createSuccess();

    protected ItemErrorCommand createError() {
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
