package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.AbstractItem.Builder;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeModeCommand extends AbstractCommand<Item<?, ?>> {

    private final Item<?, ?> item;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    public ChangeModeCommand(FileRepository repository, AccessChecker accessChecker, Item<?, ?> item,
                             AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        super(repository, accessChecker);
        this.item = item;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canChangeMode(item, actor);
    }

    @Override
    public Item<?, ?> execute(User actor) {
        Builder<? extends Item<?, ?>, ?> builder = item.copyBuilder();
        if (ownerAccess != null) {
            builder.withOwnerAccess(ownerAccess);
        }
        if (groupAccess != null) {
            builder.withGroupAccess(groupAccess);
        }
        if (otherAccess != null) {
            builder.withOtherAccess(otherAccess);
        }
        return repository.save(builder.build());
    }

    @Override
    public Error handleError(User actor) {
        return new Error(CommandType.CHMOD, new Input(item, ownerAccess, groupAccess, otherAccess), actor);
    }
}
