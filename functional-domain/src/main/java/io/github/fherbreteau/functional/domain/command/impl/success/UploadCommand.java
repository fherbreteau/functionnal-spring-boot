package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

public class UploadCommand extends AbstractSuccessCommand {
    private final File item;
    private final byte[] content;

    public UploadCommand(FileRepository repository, File item, byte[] content) {
        super(repository);
        this.item = item;
        this.content = content;
    }

    @Override
    public Output execute(User actor) {
        repository.writeContent(item, content);
        return new Output(null);
    }
}
