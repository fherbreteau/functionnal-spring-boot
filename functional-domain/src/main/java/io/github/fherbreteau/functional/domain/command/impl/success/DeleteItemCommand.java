package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;

public class DeleteItemCommand extends AbstractSuccessItemCommand<Void> {
    private final ContentRepository contentRepository;
    private final AccessUpdater accessUpdater;
    private final Item item;

    public DeleteItemCommand(ItemRepository repository, ContentRepository contentRepository,
                             AccessUpdater accessUpdater, Item item) {
        super(repository);
        this.contentRepository = contentRepository;
        this.accessUpdater = accessUpdater;
        this.item = item;
    }

    @Override
    public Output<Void> execute(User actor) {
        repository.delete(item);
        accessUpdater.deleteItem(item);
        if (item instanceof File file) {
            return contentRepository.deleteContent(file);
        }
        return Output.success(null);
    }
}
