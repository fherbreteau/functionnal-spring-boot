package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckCreateFileCommand;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckCreateFolderCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateItemCommandFactory implements ItemCommandFactory {

    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return (type == ItemCommandType.TOUCH || type == ItemCommandType.MKDIR) && isValid(itemInput);
    }

    private boolean isValid(ItemInput itemInput) {
        return itemInput.getItem() instanceof Folder && itemInput.getName() != null;
    }

    @Override
    public CheckCommand<Output> createCommand(FileRepository repository, AccessChecker accessChecker,
                                              ContentRepository contentRepository, ItemCommandType type,
                                              ItemInput itemInput) {
        if (type == ItemCommandType.TOUCH) {
            return new CheckCreateFileCommand(repository, accessChecker, itemInput.getName(),
                    (Folder) itemInput.getItem());
        }
        return new CheckCreateFolderCommand(repository, accessChecker, itemInput.getName(),
                (Folder) itemInput.getItem());
    }
}
