package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class UploadCommand extends AbstractCommand<Void> {
    private final File item;
    private final byte[] content;

    public UploadCommand(FileRepository repository, AccessChecker accessChecker, File item, byte[] content) {
        super(repository, accessChecker);
        this.item = item;
        this.content = content;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canWrite(item, actor);
    }

    @Override
    public Void execute(User actor) {
        repository.writeContent(item, content);
        return null;
    }

    @Override
    public Error handleError(User actor) {
        Input input = Input.builder(item).withContent(content).build();
        return new Error(CommandType.UPLOAD, input, actor);
    }
}
