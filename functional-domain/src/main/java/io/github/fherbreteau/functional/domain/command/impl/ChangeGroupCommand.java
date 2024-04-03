package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeGroupCommand<I extends Item<I, ?>> extends AbstractCommand<I> {

    private final I item;

    private final Group newGroup;

    public ChangeGroupCommand(FileRepository repository, AccessChecker accessChecker, I item, Group newGroup) {
        super(repository, accessChecker);
        this.item = item;
        this.newGroup = newGroup;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canChangeGroup(item, actor);
    }

    @Override
    public I execute(User actor) {
        I newItem = item.copyBuilder().withGroup(newGroup).build();
        return repository.save(newItem);
    }

    @Override
    public Error handleError(User actor) {
        Input input = Input.builder(item).withGroup(newGroup).build();
        return new Error(CommandType.CHGRP, input, actor);
    }
}
