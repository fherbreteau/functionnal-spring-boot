package io.github.fherbreteau.functional.domain.command.impl.success;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class ChangeOwnerCommand extends AbstractModifyItemCommand<Item> {

    private final Item item;

    private final User newOwner;

    public ChangeOwnerCommand(ItemRepository repository, AccessUpdater accessUpdater, Item item, User newOwner) {
        super(repository, accessUpdater);
        this.item = item;
        this.newOwner = newOwner;
    }

    @Override
    public Output<Item> execute(User actor) {
        debug(logger,  "Building new item with new owner {0}", newOwner);
        Item newItem = item.copyBuilder().withOwner(newOwner).build();
        return Output.success(repository.update(accessUpdater.updateOwner(newItem, item.getOwner())));
    }
}
