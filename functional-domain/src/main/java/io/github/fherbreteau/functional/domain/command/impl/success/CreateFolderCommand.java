package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateFolderCommand extends AbstractSuccessCommand {

    private final String name;
    private final Folder parent;

    public CreateFolderCommand(FileRepository repository, String name, Folder parent) {
        super(repository);
        this.name = name;
        this.parent = parent;
    }

    @Override
    public Output execute(User actor) {
        Folder newFolder = Folder.builder()
                .withName(name)
                .withParent(parent)
                .withOwner(actor)
                .withOwnerAccess(AccessRight.accessRight(true, true, true))
                .withGroupAccess(AccessRight.accessRight(true, false, true))
                .withOtherAccess(AccessRight.accessRight(true, false, true))
                .build();
        return new Output(repository.save(newFolder));
    }
}
