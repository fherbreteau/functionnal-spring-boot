package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckListChildrenCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.List;

public class ListChildrenCommandFactory implements ItemCommandFactory<List<Item>> {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.LIST && itemInput.getItem() instanceof Folder;
    }

    @Override
    public CheckCommand<List<Item>> createCommand(FileRepository repository, ContentRepository contentRepository,
                                                           AccessChecker accessChecker, AccessUpdater accessUpdater,
                                                           ItemCommandType type, ItemInput itemInput) {
        return new CheckListChildrenCommand(repository, accessChecker, (Folder) itemInput.getItem());
    }
}
