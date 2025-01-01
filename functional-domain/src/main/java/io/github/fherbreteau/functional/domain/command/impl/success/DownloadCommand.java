package io.github.fherbreteau.functional.domain.command.impl.success;

import java.io.InputStream;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;

public class DownloadCommand extends AbstractSuccessItemCommand<InputStream> {

    private final ContentRepository contentRepository;
    private final File item;

    public DownloadCommand(ItemRepository repository, ContentRepository contentRepository, File item) {
        super(repository);
        this.contentRepository = contentRepository;
        this.item = item;
    }

    @Override
    public Output<InputStream> execute(User actor) {
        logger.debug("Download file {}", item);
        return contentRepository.readContent(item);
    }
}
