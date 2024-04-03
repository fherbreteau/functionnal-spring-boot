package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateFileCommand extends AbstractCreateCommand<File> {

    public CreateFileCommand(FileRepository repository, AccessChecker accessChecker, String name, Folder parent) {
        super(repository, accessChecker, name, parent);
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canWrite(parent, actor) && !repository.exists(parent, name);
    }

    @Override
    public File execute(User actor) {
        File newFolder = File.builder()
                .withName(name)
                .withParent(parent)
                .withOwner(actor)
                .withOwnerAccess(AccessRight.accessRight(true, true, false))
                .withGroupAccess(AccessRight.accessRight(true, false, false))
                .withOtherAccess(AccessRight.accessRight(true, false, false))
                .build();
        return repository.save(newFolder);
    }

    @Override
    public Error handleError(User actor) {
        Input input = Input.builder(parent)
                .withName(name)
                .build();
        return new Error(CommandType.TOUCH, input, actor);
    }
}
