package io.github.fherbreteau.functional.domain.command.impl.success;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.time.LocalDateTime;

import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class MoveItemCommand extends AbstractModifyItemCommand<Item> {
    private final Item source;
    private final Item destination;

    public MoveItemCommand(ItemRepository repository, AccessUpdater accessUpdater,
                           Item source, Item destination) {
        super(repository, accessUpdater);
        this.source = source;
        this.destination = destination;
    }

    @Override
    public Output<Item> execute(User actor) {
        debug(logger,  "Moving item {0} to {1}", source, destination);
        Item newItem;
        if (source.isFolder()) {
            newItem = source.copyBuilder()
                    .withName(getDestinationName())
                    .withParent(destination.getParent())
                    .withLastModified(LocalDateTime.now())
                    .withOwner(actor)
                    .build();
        } else {
            Folder parent = getDestinationFolder();
            if (!repository.exists(parent)) {
                parent = accessUpdater.createItem(repository.create(parent.copyBuilder()
                        .withLastModified(LocalDateTime.now())
                        .withLastAccessed(LocalDateTime.now())
                        .withOwner(actor)
                        .build()));
            }
            newItem = source.copyBuilder()
                    .withName(getDestinationName())
                    .withParent(parent)
                    .withLastModified(LocalDateTime.now())
                    .withOwner(actor)
                    .build();
        }
        return Output.success(repository.update(newItem));
    }

    private Folder getDestinationFolder() {
        return destination.isFile() ? destination.getParent() : (Folder) destination;
    }

    private String getDestinationName() {
        return destination.isFile() ? destination.getName() : source.getName();
    }
}
