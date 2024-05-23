package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCheckCreateItemCommand<C extends Command<Output>> extends AbstractCheckItemCommand<C> {
    protected final String name;
    protected final Folder parent;
    protected final AccessUpdater accessUpdater;

    protected AbstractCheckCreateItemCommand(FileRepository repository, AccessChecker accessChecker,
                                             AccessUpdater accessUpdater, String name, Folder parent) {
        super(repository, accessChecker);
        this.accessUpdater = accessUpdater;
        this.name = name;
        this.parent = parent;
    }

    @Override
    protected final List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canWrite(parent, actor)) {
            reasons.add(String.format(getCantWriteFormat(), actor, parent));
        }
        if (repository.exists(parent, name)) {
            reasons.add(String.format("%s already exists in  %s", name, parent));
        }
        return reasons;
    }

    protected abstract String getCantWriteFormat();

    @Override
    protected final ItemErrorCommand createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(parent)
                .withName(name)
                .build();
        return new ItemErrorCommand(getType(), itemInput, reasons);
    }

    protected abstract ItemCommandType getType();
}
