package io.github.fherbreteau.functional.domain.command.impl.check;

import java.util.ArrayList;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CheckChangeGroupCommand extends AbstractCheckItemCommand<Item, ChangeGroupCommand> {

    private final AccessUpdater accessUpdater;
    private final Item item;
    private final Group newGroup;

    public CheckChangeGroupCommand(ItemRepository repository, AccessChecker accessChecker, AccessUpdater accessUpdater,
                                   Item item, Group newGroup) {
        super(repository, accessChecker);
        this.accessUpdater = accessUpdater;
        this.item = item;
        this.newGroup = newGroup;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canChangeGroup(item, actor)) {
            reasons.add(String.format("%s can't change group of %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected ChangeGroupCommand createSuccess() {

        return new ChangeGroupCommand(repository, accessUpdater, item, newGroup);
    }

    @Override
    protected ItemErrorCommand<Item> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item).withGroup(newGroup).build();
        return new ItemErrorCommand<>(ItemCommandType.CHGRP, itemInput, reasons);
    }
}
