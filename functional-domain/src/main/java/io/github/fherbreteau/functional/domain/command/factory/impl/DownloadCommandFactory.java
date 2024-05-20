package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDownloadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class DownloadCommandFactory implements ItemCommandFactory {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.DOWNLOAD && itemInput.getItem() instanceof File;
    }

    @Override
    public CheckCommand<Output> createCommand(FileRepository repository, AccessChecker accessChecker,
                                              ContentRepository contentRepository, ItemCommandType type,
                                              ItemInput itemInput) {
        return new CheckDownloadCommand(repository, accessChecker, contentRepository, (File) itemInput.getItem());
    }
}
