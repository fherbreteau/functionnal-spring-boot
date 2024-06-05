package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ItemRepository;

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
        Item newItem = item.copyBuilder().withGroup(newGroup).build();
        return Output.success(repository.save(accessUpdater.updateGroup(newItem, item.getGroup())));
    }
}
