package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDownloadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;

import java.io.InputStream;

public class DownloadCommandFactory implements ItemCommandFactory<InputStream> {
    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.DOWNLOAD && itemInput.getItem() instanceof File;
    }

    @Override
    public CheckCommand<InputStream> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                                   AccessChecker accessChecker, AccessUpdater accessUpdater,
                                                   ItemCommandType type, ItemInput itemInput) {
        return new CheckDownloadCommand(repository, accessChecker, contentRepository, (File) itemInput.getItem());
    }
}
