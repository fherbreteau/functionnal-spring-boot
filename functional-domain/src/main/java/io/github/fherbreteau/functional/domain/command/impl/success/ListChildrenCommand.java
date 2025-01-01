package io.github.fherbreteau.functional.domain.command.impl.success;

import java.util.List;

import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;

public class ListChildrenCommand extends AbstractSuccessItemCommand<List<Item>> {

    private final Folder folder;

    public ListChildrenCommand(ItemRepository repository, Folder folder) {
        super(repository);
        this.folder = folder;
    }

    @Override
    public Output<List<Item>> execute(User actor) {
        logger.debug("Listing content of folder {}", folder);
        return Output.success(repository.findByParentAndUser(folder, actor));
    }

}
