package io.github.fherbreteau.functional.domain.command.impl.check;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import io.github.fherbreteau.functional.domain.command.impl.success.CreateFolderCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CheckCreateFolderCommand extends AbstractCheckCreateItemCommand<Item, CreateFolderCommand> {

    public CheckCreateFolderCommand(ItemRepository repository, AccessChecker accessChecker, AccessUpdater accessUpdater,
                                    String name, Folder parent) {
        super(repository, accessChecker, accessUpdater, name, parent);
    }

    @Override
    protected String getCantWriteFormat() {
        return "%s can't create folder in %s";
    }

    @Override
    protected CreateFolderCommand createSuccess() {
        debug(logger,  "Creating execute command");
        return new CreateFolderCommand(repository, accessUpdater, name, parent);
    }

    @Override
    protected ItemCommandType getType() {
        return ItemCommandType.MKDIR;
    }
}
