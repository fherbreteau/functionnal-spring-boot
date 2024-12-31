package io.github.fherbreteau.functional.domain.command.factory.impl;

import static java.lang.System.Logger.Level.DEBUG;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckChangeGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class ChangeGroupCommandFactory implements ItemCommandFactory<Item> {
    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.CHGRP && validInput(itemInput);
    }

    private boolean validInput(ItemInput itemInput) {
        return itemInput.getItem() != null && itemInput.getGroup() != null;
    }

    @Override
    public CheckCommand<Item> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                            AccessChecker accessChecker, AccessUpdater accessUpdater,
                                            ItemCommandType type, ItemInput itemInput) {
        logger.log(DEBUG, "Creating check command");
        return new CheckChangeGroupCommand(repository, accessChecker, accessUpdater, itemInput.getItem(),
                itemInput.getGroup());
    }
}
