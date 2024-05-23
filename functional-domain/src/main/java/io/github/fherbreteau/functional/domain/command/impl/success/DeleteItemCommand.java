package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class DeleteItemCommand extends AbstractSuccessItemCommand {
    private final ContentRepository contentRepository;
    private final AccessUpdater accessUpdater;
    private final Item item;

    public DeleteItemCommand(FileRepository repository, ContentRepository contentRepository,
                             AccessUpdater accessUpdater, Item item) {
        super(repository);
        this.contentRepository = contentRepository;
        this.accessUpdater = accessUpdater;
        this.item = item;
    }

    @Override
    public Output execute(User actor) {
        if (item instanceof File file) {
            contentRepository.deleteContent(file);
        }
        return new Output(accessUpdater.deleteItem(repository.delete(item)));
    }
}
