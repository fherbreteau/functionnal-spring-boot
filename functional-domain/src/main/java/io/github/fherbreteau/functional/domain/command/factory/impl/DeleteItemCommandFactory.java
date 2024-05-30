package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDeleteItemCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import static java.util.Objects.nonNull;

public class DeleteItemCommandFactory implements ItemCommandFactory<Void> {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.DELETE && nonNull(itemInput.getItem());
    }

    @Override
    public CheckCommand<Void> createCommand(FileRepository repository, ContentRepository contentRepository,
                                              AccessChecker accessChecker, AccessUpdater accessUpdater,
                                              ItemCommandType type, ItemInput itemInput) {
        return new CheckDeleteItemCommand(repository, contentRepository, accessChecker, accessUpdater,
                itemInput.getItem());
    }
}
