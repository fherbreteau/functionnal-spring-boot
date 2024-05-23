package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.CreateFolderCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckCreateFolderCommand extends AbstractCheckCreateItemCommand<CreateFolderCommand> {

    public CheckCreateFolderCommand(FileRepository repository, AccessChecker accessChecker, AccessUpdater accessUpdater,
                                    String name, Folder parent) {
        super(repository, accessChecker, accessUpdater, name, parent);
    }

    @Override
    protected String getCantWriteFormat() {
        return "%s can't create folder in %s";
    }

    @Override
    protected CreateFolderCommand createSuccess() {
        return new CreateFolderCommand(repository, accessUpdater, name, parent);
    }

    @Override
    protected ItemCommandType getType() {
        return ItemCommandType.MKDIR;
    }
}
