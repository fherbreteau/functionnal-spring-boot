package io.github.fherbreteau.functional.domain.command.factory.impl;

import static java.lang.System.Logger.Level.DEBUG;
import static java.util.Objects.nonNull;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDeleteItemCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class DeleteItemCommandFactory implements ItemCommandFactory<Void> {
    private final System.Logger logger = System.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.DELETE && nonNull(itemInput.getItem());
    }

    @Override
    public CheckCommand<Void> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                            AccessChecker accessChecker, AccessUpdater accessUpdater,
                                            ItemCommandType type, ItemInput itemInput) {
        logger.log(DEBUG, "Creating check command");
        return new CheckDeleteItemCommand(repository, contentRepository, accessChecker, accessUpdater,
                itemInput.getItem());
    }
}
