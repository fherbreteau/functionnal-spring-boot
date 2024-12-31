package io.github.fherbreteau.functional.domain.command.impl.success;

import static java.lang.System.Logger.Level.DEBUG;

import io.github.fherbreteau.functional.domain.entities.AbstractItem.AbstractBuilder;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class ChangeModeCommand extends AbstractModifyItemCommand<Item> {

    private final Item item;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    public ChangeModeCommand(ItemRepository repository, AccessUpdater accessUpdater, Item item, AccessRight ownerAccess,
                             AccessRight groupAccess, AccessRight otherAccess) {
        super(repository, accessUpdater);
        this.item = item;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
    }

    @Override
    public Output<Item> execute(User actor) {
        logger.log(DEBUG, "Building new item with new access right {0}{1}{2}", ownerAccess, groupAccess, otherAccess);
        AbstractBuilder<?, ?> builder = item.copyBuilder();
        if (ownerAccess != null) {
            builder.withOwnerAccess(ownerAccess);
            builder = accessUpdater.updateOwnerAccess(builder.build(), item.getOwnerAccess()).copyBuilder();
        }
        if (groupAccess != null) {
            builder.withGroupAccess(groupAccess);
            builder = accessUpdater.updateGroupAccess(builder.build(), item.getGroupAccess()).copyBuilder();
        }
        if (otherAccess != null) {
            builder.withOtherAccess(otherAccess);
            builder = accessUpdater.updateOtherAccess(builder.build(), item.getOtherAccess()).copyBuilder();
        }
        return Output.success(repository.update(builder.build()));
    }
}
