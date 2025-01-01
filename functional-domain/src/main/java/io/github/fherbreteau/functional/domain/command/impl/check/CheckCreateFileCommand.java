package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.CreateFileCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

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
        logger.debug("Creating execute command");
        return new CreateFileCommand(repository, contentRepository, accessUpdater, name, parent);
    }

    @Override
    protected ItemCommandType getType() {
        return ItemCommandType.TOUCH;
    }
}
