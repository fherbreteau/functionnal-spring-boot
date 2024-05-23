package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateFileCommand extends AbstractSuccessItemCommand {

    private final String name;
    private final Folder parent;

    public CreateFileCommand(FileRepository repository, String name, Folder parent) {
        super(repository);
        this.name = name;
        this.parent = parent;
    }

    @Override
    public Output execute(User actor) {
        File newFolder = File.builder()
                .withName(name)
                .withParent(parent)
                .withOwner(actor)
                .withOwnerAccess(AccessRight.readWrite())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
        return new Output(repository.save(newFolder));
    }
}
