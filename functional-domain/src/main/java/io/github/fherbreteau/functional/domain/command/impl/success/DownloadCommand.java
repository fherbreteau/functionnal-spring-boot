package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class DownloadCommand extends AbstractSuccessItemCommand {

    private final ContentRepository contentRepository;
    private final File item;

    public DownloadCommand(FileRepository repository, ContentRepository contentRepository, File item) {
        super(repository);
        this.contentRepository = contentRepository;
        this.item = item;
    }

    @Override
    public Output execute(User actor) {
        return new Output(contentRepository.readContent(item));
    }
}
