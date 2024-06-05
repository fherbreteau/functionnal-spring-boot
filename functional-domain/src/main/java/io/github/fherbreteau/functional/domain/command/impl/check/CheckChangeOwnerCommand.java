package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeOwnerCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class CheckChangeOwnerCommand extends AbstractCheckItemCommand<Item, ChangeOwnerCommand> {

    private final AccessUpdater accessUpdater;
    private final Item item;
    private final User newOwner;

    public CheckChangeOwnerCommand(ItemRepository repository, AccessChecker accessChecker, AccessUpdater accessUpdater,
                                   Item item, User newOwner) {
        super(repository, accessChecker);
        this.accessUpdater = accessUpdater;
        this.item = item;
        this.newOwner = newOwner;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canChangeOwner(item, actor)) {
            reasons.add(String.format("%s can't change owner of %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected ChangeOwnerCommand createSuccess() {
        return new ChangeOwnerCommand(repository, accessUpdater, item, newOwner);
    }

    @Override
    protected ItemErrorCommand<Item> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item)
                .withUser(newOwner)
                .build();
        return new ItemErrorCommand<>(ItemCommandType.CHOWN, itemInput, reasons);
    }
}
