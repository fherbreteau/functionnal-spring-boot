package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.io.InputStream;

public class UploadCommand extends AbstractSuccessCommand {
    private final ContentRepository contentRepository;
    private final File item;
    private final InputStream content;
    private final String contentType;

    public UploadCommand(FileRepository repository, ContentRepository contentRepository, File item, InputStream content, String contentType) {
        super(repository);
        this.contentRepository = contentRepository;
        this.item = item;
        this.content = content;
        this.contentType = contentType;
    }

    @Override
    public Output execute(User actor) {
        contentRepository.writeContent(item, content);
        File newItem = item.copyBuilder().withContentType(contentType).build();
        return new Output(repository.save(newItem));
    }
}
