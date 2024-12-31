package io.github.fherbreteau.functional.domain.command.impl.success;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class ChangeGroupCommand extends AbstractModifyItemCommand<Item> {

    private final Item item;
    private final Group newGroup;

    public ChangeGroupCommand(ItemRepository repository, AccessUpdater accessUpdater, Item item, Group newGroup) {
        super(repository, accessUpdater);
        this.item = item;
        this.newGroup = newGroup;
    }

    @Override
    public Output<Item> execute(User actor) {
        debug(logger,  "Building new item with group {0}", newGroup);
        Item newItem = item.copyBuilder().withGroup(newGroup).build();
        return Output.success(repository.update(accessUpdater.updateGroup(newItem, item.getGroup())));
    }
}
