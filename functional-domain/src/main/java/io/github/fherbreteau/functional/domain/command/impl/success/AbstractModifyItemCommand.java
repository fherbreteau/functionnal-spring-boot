package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.driven.*;

public abstract class AbstractModifyItemCommand extends AbstractSuccessItemCommand {

    protected final AccessUpdater accessUpdater;

    protected AbstractModifyItemCommand(FileRepository repository, AccessUpdater accessUpdater) {
        super(repository);
        this.accessUpdater = accessUpdater;
    }
}
