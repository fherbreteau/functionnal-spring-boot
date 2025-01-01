package io.github.fherbreteau.functional.domain.command.impl.check;

import static io.github.fherbreteau.functional.domain.Logging.error;

import java.util.List;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public abstract class AbstractCheckItemCommand<T, C extends Command<Output<T>>> extends AbstractCheckCommand<T, C, ItemErrorCommand<T>> {
    protected final ItemRepository repository;

    protected final AccessChecker accessChecker;

    protected AbstractCheckItemCommand(ItemRepository repository, AccessChecker accessChecker) {
        this.repository = repository;
        this.accessChecker = accessChecker;
    }

    protected ItemErrorCommand<T> createError(List<String> reason) {
        error(logger, "Unable to create Command");
        throw new UnsupportedOperationException("Unsupported Command always succeed");
    }
}
