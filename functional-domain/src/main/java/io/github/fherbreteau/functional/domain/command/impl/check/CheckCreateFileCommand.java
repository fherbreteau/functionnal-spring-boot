package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.CreateFileCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;

public class CheckCreateFileCommand extends AbstractCheckCreateItemCommand<Item, CreateFileCommand> {

    private final ContentRepository contentRepository;

    public CheckCreateFileCommand(ItemRepository repository, ContentRepository contentRepository,
                                  AccessChecker accessChecker, AccessUpdater accessUpdater, String name,
                                  Folder parent) {
        super(repository, accessChecker, accessUpdater, name, parent);
        this.contentRepository = contentRepository;
    }

    @Override
    protected String getCantWriteFormat() {
        return "%s can't create file in %s";
    }

    @Override
    protected CreateFileCommand createSuccess() {
        return new CreateFileCommand(repository, contentRepository, accessUpdater, name, parent);
    }

    @Override
    protected ItemCommandType getType() {
        return ItemCommandType.TOUCH;
    }
}
