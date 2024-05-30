package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.List;

public class ListChildrenCommand extends AbstractSuccessItemCommand<List<Item>> {

    private final Folder folder;

    public ListChildrenCommand(FileRepository repository, Folder folder) {
        super(repository);
        this.folder = folder;
    }

    @Override
    public Output<List<Item>> execute(User actor) {
        return Output.success(repository.findByParentAndUser(folder, actor));
    }

}
