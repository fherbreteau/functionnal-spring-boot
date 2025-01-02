package io.github.fherbreteau.functional.domain.command.impl.check;

import java.util.ArrayList;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public abstract class AbstractCheckCreateItemCommand<T, C extends Command<Output<T>>> extends AbstractCheckItemCommand<T, C> {
    protected final String name;
    protected final Folder parent;
    protected final AccessUpdater accessUpdater;

    protected AbstractCheckCreateItemCommand(ItemRepository repository, AccessChecker accessChecker,
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
            reasons.add(String.format(getCantWriteFormat(), actor, parent.getHandle()));
        }
        if (repository.exists(parent, name)) {
            reasons.add(String.format("%s already exists in %s", name, parent.getHandle()));
        }
        return reasons;
    }

    protected abstract String getCantWriteFormat();

    @Override
    protected final ItemErrorCommand<T> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(parent)
                .withName(name)
                .build();
        return new ItemErrorCommand<>(getType(), itemInput, reasons);
    }

    protected abstract ItemCommandType getType();
}
