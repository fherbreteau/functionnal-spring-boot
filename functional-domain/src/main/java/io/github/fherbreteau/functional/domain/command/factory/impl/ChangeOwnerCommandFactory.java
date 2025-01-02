package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckChangeOwnerCommand;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeOwnerCommandFactory implements ItemCommandFactory<Item> {
    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.CHOWN && isValid(itemInput);
    }

    private boolean isValid(ItemInput itemInput) {
        return itemInput.getItem() != null && itemInput.getUser() != null;
    }

    @Override
    public CheckCommand<Item> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                            AccessChecker accessChecker, AccessUpdater accessUpdater,
                                            ItemCommandType type, ItemInput itemInput) {
        logger.debug("Creating check command");
        return new CheckChangeOwnerCommand(repository, accessChecker, accessUpdater, itemInput.getItem(),
                itemInput.getUser());
    }
}
