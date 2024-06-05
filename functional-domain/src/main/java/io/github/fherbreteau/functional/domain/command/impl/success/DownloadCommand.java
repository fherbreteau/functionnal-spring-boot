package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.io.InputStream;

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
        return contentRepository.readContent(item);
    }
}
