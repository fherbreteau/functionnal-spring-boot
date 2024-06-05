package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.driven.*;

public abstract class AbstractModifyItemCommand<T> extends AbstractSuccessItemCommand<T> {

    protected final AccessUpdater accessUpdater;

    protected AbstractModifyItemCommand(ItemRepository repository, AccessUpdater accessUpdater) {
        super(repository);
        this.accessUpdater = accessUpdater;
    }
}
