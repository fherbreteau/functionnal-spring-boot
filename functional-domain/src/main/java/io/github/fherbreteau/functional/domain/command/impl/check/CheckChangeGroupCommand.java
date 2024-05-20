package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckChangeGroupCommand extends AbstractCheckItemCommand<ChangeGroupCommand> {

    private final Item item;

    private final Group newGroup;

    public CheckChangeGroupCommand(FileRepository repository, AccessChecker accessChecker, Item item, Group newGroup) {
        super(repository, accessChecker);
        this.item = item;
        this.newGroup = newGroup;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canChangeGroup(item, actor);
    }

    @Override
    protected ChangeGroupCommand createSuccess() {
        return new ChangeGroupCommand(repository, item, newGroup);
    }

    @Override
    protected ItemErrorCommand createError() {
        ItemInput itemInput = ItemInput.builder(item).withGroup(newGroup).build();
        return new ItemErrorCommand(ItemCommandType.CHGRP, itemInput);
    }
}
