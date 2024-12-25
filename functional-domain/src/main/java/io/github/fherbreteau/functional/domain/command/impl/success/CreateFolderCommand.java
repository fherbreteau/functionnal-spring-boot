package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CreateFolderCommand extends AbstractModifyItemCommand<Item> {

    private final String name;
    private final Folder parent;

    public CreateFolderCommand(ItemRepository repository, AccessUpdater accessUpdater, String name, Folder parent) {
        super(repository, accessUpdater);
        this.name = name;
        this.parent = parent;
    }

    @Override
    public Output<Item> execute(User actor) {
        Folder newFolder = Folder.builder()
                .withName(name)
                .withParent(parent)
                .withOwner(actor)
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readExecute())
                .withOtherAccess(AccessRight.none())
                .build();
        return Output.success(repository.create(accessUpdater.createItem(newFolder)));
    }
}
