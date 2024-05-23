package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckChangeOwnerCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeOwnerCommandFactory implements ItemCommandFactory {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.CHOWN && isValid(itemInput);
    }

    private boolean isValid(ItemInput itemInput) {
        return itemInput.getItem() != null && itemInput.getUser() != null;
    }

    @Override
    public CheckCommand<Output> createCommand(FileRepository repository, ContentRepository contentRepository,
                                              AccessChecker accessChecker, AccessUpdater accessUpdater,
                                              ItemCommandType type, ItemInput itemInput) {
        return new CheckChangeOwnerCommand(repository, accessChecker, accessUpdater, itemInput.getItem(),
                itemInput.getUser());
    }
}
