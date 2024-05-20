package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckChangeModeCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeModeCommandFactory implements ItemCommandFactory {

    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.CHMOD && isValid(itemInput);
    }

    private boolean isValid(ItemInput itemInput) {
        return itemInput.getItem() != null && itemInput.hasAccess();
    }

    @Override
    public CheckCommand<Output> createCommand(FileRepository repository, AccessChecker accessChecker,
                                              ContentRepository contentRepository, ItemCommandType type,
                                              ItemInput itemInput) {
        return new CheckChangeModeCommand(repository, accessChecker, itemInput.getItem(), itemInput.getOwnerAccess(),
                itemInput.getGroupAccess(), itemInput.getOtherAccess());
    }
}
