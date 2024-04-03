package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.AbstractItem.AbstractBuilder;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeModeCommand<I extends Item<I, ?>> extends AbstractCommand<I> {

    private final I item;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    public ChangeModeCommand(FileRepository repository, AccessChecker accessChecker, I item, AccessRight ownerAccess,
                             AccessRight groupAccess, AccessRight otherAccess) {
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
    public I execute(User actor) {
        AbstractBuilder<I, ?> builder = item.copyBuilder();
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
        Input input = Input.builder(item)
                .withOwnerAccess(ownerAccess)
                .withGroupAccess(groupAccess)
                .withOtherAccess(otherAccess)
                .build();
        return new Error(CommandType.CHMOD, input, actor);
    }
}
