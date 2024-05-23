package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckUploadCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class UploadCommandFactory implements ItemCommandFactory {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.UPLOAD && itemInput.getItem() instanceof File && itemInput.getContent() != null
                && itemInput.getContentType() != null;
    }

    @Override
    public CheckCommand<Output> createCommand(FileRepository repository, ContentRepository contentRepository,
                                              AccessChecker accessChecker, AccessUpdater accessUpdater,
                                              ItemCommandType type, ItemInput itemInput) {
        return new CheckUploadCommand(repository, accessChecker, contentRepository, (File) itemInput.getItem(),
                itemInput.getContent(), itemInput.getContentType());
    }
}
