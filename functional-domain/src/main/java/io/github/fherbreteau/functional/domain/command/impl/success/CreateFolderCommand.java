package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateFolderCommand extends AbstractModifyItemCommand {

    private final String name;
    private final Folder parent;

    public CreateFolderCommand(FileRepository repository, AccessUpdater accessUpdater, String name, Folder parent) {
        super(repository, accessUpdater);
        this.name = name;
        this.parent = parent;
    }

    @Override
    public Output execute(User actor) {
        Folder newFolder = Folder.builder()
                .withName(name)
                .withParent(parent)
                .withOwner(actor)
                .withOwnerAccess(AccessRight.full())
                .withGroupAccess(AccessRight.readExecute())
                .withOtherAccess(AccessRight.none())
                .build();
        return new Output(repository.save(accessUpdater.createItem(newFolder)));
    }
}
