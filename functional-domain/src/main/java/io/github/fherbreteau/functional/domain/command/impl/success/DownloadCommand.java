package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

public class DownloadCommand extends AbstractSuccessCommand {

    private final File item;

    public DownloadCommand(FileRepository repository, File item) {
        super(repository);
        this.item = item;
    }

    @Override
    public Output execute(User actor) {
        return new Output(repository.readContent(item));
    }
}
