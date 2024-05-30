package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateFolderCommand extends AbstractModifyItemCommand<Item> {

    private final String name;
    private final Folder parent;

    public CreateFolderCommand(FileRepository repository, AccessUpdater accessUpdater, String name, Folder parent) {
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
        return Output.success(repository.save(accessUpdater.createItem(newFolder)));
    }
}
