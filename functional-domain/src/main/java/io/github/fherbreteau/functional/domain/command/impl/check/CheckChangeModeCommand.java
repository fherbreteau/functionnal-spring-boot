package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeModeCommand;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckChangeModeCommand extends AbstractCheckItemCommand<ChangeModeCommand> {

    private final Item item;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    public CheckChangeModeCommand(FileRepository repository, AccessChecker accessChecker, Item item,
                                     AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        super(repository, accessChecker);
        this.item = item;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canChangeMode(item, actor);
    }

    @Override
    protected ChangeModeCommand createSuccess() {
        return new ChangeModeCommand(repository, item, ownerAccess, groupAccess, otherAccess);
    }

    @Override
    protected ItemErrorCommand createError() {
        ItemInput itemInput = ItemInput.builder(item)
                .withOwnerAccess(ownerAccess)
                .withGroupAccess(groupAccess)
                .withOtherAccess(otherAccess)
                .build();
        return new ItemErrorCommand(ItemCommandType.CHMOD, itemInput);
    }
}
