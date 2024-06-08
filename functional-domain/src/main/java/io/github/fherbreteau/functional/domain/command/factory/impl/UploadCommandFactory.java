package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUploadCommand;
import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;

public class UploadCommandFactory implements ItemCommandFactory<Item> {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.UPLOAD && itemInput.getItem() instanceof File && itemInput.getContent() != null
                && itemInput.getContentType() != null;
    }

    @Override
    public CheckCommand<Item> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                            AccessChecker accessChecker, AccessUpdater accessUpdater,
                                            ItemCommandType type, ItemInput itemInput) {
        return new CheckUploadCommand(repository, accessChecker, contentRepository, (File) itemInput.getItem(),
                itemInput.getContent(), itemInput.getContentType());
    }
}
