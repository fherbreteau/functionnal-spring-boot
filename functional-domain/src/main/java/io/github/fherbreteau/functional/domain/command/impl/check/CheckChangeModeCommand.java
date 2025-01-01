package io.github.fherbreteau.functional.domain.command.impl.check;

import java.util.ArrayList;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeModeCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CheckChangeModeCommand extends AbstractCheckItemCommand<Item, ChangeModeCommand> {

    private final AccessUpdater accessUpdater;
    private final Item item;
    private final AccessRight ownerAccess;
    private final AccessRight groupAccess;
    private final AccessRight otherAccess;

    public CheckChangeModeCommand(ItemRepository repository, AccessChecker accessChecker, AccessUpdater accessUpdater,
                                  Item item, AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        super(repository, accessChecker);
        this.accessUpdater = accessUpdater;
        this.item = item;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canChangeMode(item, actor)) {
            reasons.add(String.format("%s can't change mode of %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected ChangeModeCommand createSuccess() {
        logger.debug("Creating execute command");
        return new ChangeModeCommand(repository, accessUpdater, item, ownerAccess, groupAccess, otherAccess);
    }

    @Override
    protected ItemErrorCommand<Item> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item)
                .withOwnerAccess(ownerAccess)
                .withGroupAccess(groupAccess)
                .withOtherAccess(otherAccess)
                .build();
        return new ItemErrorCommand<>(ItemCommandType.CHMOD, itemInput, reasons);
    }
}
