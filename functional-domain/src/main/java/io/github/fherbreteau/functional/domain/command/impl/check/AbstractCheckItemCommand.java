package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;

import java.util.List;

public abstract class AbstractCheckItemCommand<T, C extends Command<Output<T>>> implements CheckCommand<T> {

    protected final ItemRepository repository;

    protected final AccessChecker accessChecker;

    protected AbstractCheckItemCommand(ItemRepository repository, AccessChecker accessChecker) {
        this.repository = repository;
        this.accessChecker = accessChecker;
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

    protected ItemErrorCommand<T> createError(List<String> reason) {
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
