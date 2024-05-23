package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeOwnerCommand extends AbstractModifyItemCommand {

    private final Item item;

    private final User newOwner;

    public ChangeOwnerCommand(FileRepository repository, AccessUpdater accessUpdater, Item item, User newOwner) {
        super(repository, accessUpdater);
        this.item = item;
        this.newOwner = newOwner;
    }

    @Override
    public Output execute(User actor) {
        Item newItem = item.copyBuilder().withOwner(newOwner).build();
        return new Output(repository.save(accessUpdater.updateOwner(newItem, item.getOwner())));
    }
}
