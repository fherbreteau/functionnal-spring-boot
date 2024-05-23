package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ListChildrenCommand extends AbstractSuccessItemCommand {

    private final Folder folder;

    public ListChildrenCommand(FileRepository repository, Folder folder) {
        super(repository);
        this.folder = folder;
    }

    @Override
    public Output execute(User actor) {
        return new Output(repository.findByParentAndUser(folder, actor));
    }

}
