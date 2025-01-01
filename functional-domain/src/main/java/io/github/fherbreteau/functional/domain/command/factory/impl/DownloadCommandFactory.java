package io.github.fherbreteau.functional.domain.command.factory.impl;

import static io.github.fherbreteau.functional.domain.Logging.debug;

import java.io.InputStream;
import java.util.logging.Logger;

import io.github.fherbreteau.functional.domain.command.CheckCommand;
import io.github.fherbreteau.functional.domain.command.factory.ItemCommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckDownloadCommand;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class DownloadCommandFactory implements ItemCommandFactory<InputStream> {
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    @Override
    public boolean supports(ItemCommandType type, ItemInput itemInput) {
        return type == ItemCommandType.DOWNLOAD && itemInput.getItem() instanceof File;
    }

    @Override
    public CheckCommand<InputStream> createCommand(ItemRepository repository, ContentRepository contentRepository,
                                                   AccessChecker accessChecker, AccessUpdater accessUpdater,
                                                   ItemCommandType type, ItemInput itemInput) {
        debug(logger,  "Creating check command");
        return new CheckDownloadCommand(repository, accessChecker, contentRepository, (File) itemInput.getItem());
    }
}
