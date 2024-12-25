package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckCreateFileCommand;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckCreateFolderCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CreateItemCommandFactory implements ItemCommandFactory<Item> {

    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return (type == ItemCommandType.TOUCH || type == ItemCommandType.MKDIR) && isValid(itemInput);
    }

    private boolean isValid(ItemInput itemInput) {
        return itemInput.getItem() instanceof Folder && itemInput.getName() != null;
    }

    @Override
    public CheckCommand<Item> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                            AccessChecker accessChecker, AccessUpdater accessUpdater,
                                            ItemCommandType type, ItemInput itemInput) {
        if (type == ItemCommandType.TOUCH) {
            return new CheckCreateFileCommand(repository, contentRepository, accessChecker, accessUpdater,
                    itemInput.getName(), (Folder) itemInput.getItem());
        }
        return new CheckCreateFolderCommand(repository, accessChecker, accessUpdater, itemInput.getName(),
                (Folder) itemInput.getItem());
    }
}
