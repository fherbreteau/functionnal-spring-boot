package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeGroupCommand extends AbstractCommand<Item<?, ?>> {

    private final Item<?, ?> item;

    private final Group newGroup;

    public ChangeGroupCommand(FileRepository repository, AccessChecker accessChecker, Item<?, ?> item, Group newGroup) {
        super(repository, accessChecker);
        this.item = item;
        this.newGroup = newGroup;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canChangeGroup(item, actor);
    }

    @Override
    public Item<?, ?> execute(User actor) {
        Item<?, ?> newItem = item.copyBuilder().withGroup(newGroup).build();
        return repository.save(newItem);
    }

    @Override
    public Error handleError(User actor) {
        return new Error(CommandType.CHGRP, new Input(item, newGroup), actor);
    }
}
