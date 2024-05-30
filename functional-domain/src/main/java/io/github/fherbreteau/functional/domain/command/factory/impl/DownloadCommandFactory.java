package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDownloadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.io.InputStream;

public class DownloadCommandFactory implements ItemCommandFactory<InputStream> {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.DOWNLOAD && itemInput.getItem() instanceof File;
    }

    @Override
    public CheckCommand<InputStream> createCommand(FileRepository repository, ContentRepository contentRepository,
                                              AccessChecker accessChecker, AccessUpdater accessUpdater,
                                              ItemCommandType type, ItemInput itemInput) {
        return new CheckDownloadCommand(repository, accessChecker, contentRepository, (File) itemInput.getItem());
    }
}
