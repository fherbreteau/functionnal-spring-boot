package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeOwnerCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckChangeOwnerCommand extends AbstractCheckItemCommand<ChangeOwnerCommand> {

    private final Item item;

    private final User newOwner;

    public CheckChangeOwnerCommand(FileRepository repository, AccessChecker accessChecker, Item item, User newOwner) {
        super(repository, accessChecker);
        this.item = item;
        this.newOwner = newOwner;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canChangeOwner(item, actor);
    }

    @Override
    protected ChangeOwnerCommand createSuccess() {
        return new ChangeOwnerCommand(repository, item, newOwner);
    }

    @Override
    protected ItemErrorCommand createError() {
        ItemInput itemInput = ItemInput.builder(item)
                .withUser(newOwner)
                .build();
        return new ItemErrorCommand(ItemCommandType.CHOWN, itemInput);
    }
}
