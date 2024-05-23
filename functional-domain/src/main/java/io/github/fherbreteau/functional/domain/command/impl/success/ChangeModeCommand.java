package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.AbstractItem.AbstractBuilder;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeModeCommand extends AbstractModifyItemCommand {

    private final Item item;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    public ChangeModeCommand(FileRepository repository, AccessUpdater accessUpdater, Item item, AccessRight ownerAccess,
                             AccessRight groupAccess, AccessRight otherAccess) {
        super(repository, accessUpdater);
        this.item = item;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
    }

    @Override
    public Output execute(User actor) {
        AbstractBuilder<?, ?> builder = item.copyBuilder();
        if (ownerAccess != null) {
            builder.withOwnerAccess(ownerAccess);
            accessUpdater.updateOwnerAccess(builder.build(), item.getOwnerAccess());
        }
        if (groupAccess != null) {
            builder.withGroupAccess(groupAccess);
            accessUpdater.updateGroupAccess(builder.build(), item.getGroupAccess());
        }
        if (otherAccess != null) {
            builder.withOtherAccess(otherAccess);
            accessUpdater.updateOtherAccess(builder.build(), item.getOtherAccess());
        }
        return new Output(repository.save(builder.build()));
    }
}
