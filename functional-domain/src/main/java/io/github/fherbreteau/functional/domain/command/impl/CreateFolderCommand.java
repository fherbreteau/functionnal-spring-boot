package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateFolderCommand extends AbstractCreateCommand<Folder> {

    public CreateFolderCommand(FileRepository repository, AccessChecker accessChecker, String name, Folder parent) {
        super(repository, accessChecker, name, parent);
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canWrite(parent, actor) && !repository.exists(parent, name);
    }

    @Override
    public Folder execute(User actor) {
        Folder newFolder = Folder.builder()
                .withName(name)
                .withParent(parent)
                .withOwner(actor)
                .withOwnerAccess(AccessRight.accessRight(true, true, true))
                .withGroupAccess(AccessRight.accessRight(true, false, true))
                .withOtherAccess(AccessRight.accessRight(true, false, true))
                .build();
        return repository.save(newFolder);
    }

    @Override
    public Error handleError(User actor) {
        return new Error(CommandType.MKDIR, new Input(parent, name), actor);
    }
}
