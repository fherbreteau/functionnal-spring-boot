package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class DownloadCommand extends AbstractCommand<byte[]> {

    private final File item;

    public DownloadCommand(FileRepository repository, AccessChecker accessChecker, File item) {
        super(repository, accessChecker);
        this.item = item;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canRead(item, actor);
    }

    @Override
    public byte[] execute(User actor) {
        return repository.readContent(item);
    }

    @Override
    public Error handleError(User actor) {
        return new Error(CommandType.DOWNLOAD, Input.builder(item).build(), actor);
    }
}
