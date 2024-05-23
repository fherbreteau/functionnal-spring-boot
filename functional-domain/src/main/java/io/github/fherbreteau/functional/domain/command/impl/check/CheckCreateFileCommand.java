package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.success.CreateFileCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckCreateFileCommand extends AbstractCheckCreateItemCommand<CreateFileCommand> {

    public CheckCreateFileCommand(FileRepository repository, AccessChecker accessChecker, String name, Folder parent) {
        super(repository, accessChecker, name, parent);
    }

    @Override
    protected String getCantWriteFormat() {
        return "%s can't create file in %s";
    }

    @Override
    protected CreateFileCommand createSuccess() {
        return new CreateFileCommand(repository, name, parent);
    }

    @Override
    protected ItemCommandType getType() {
        return ItemCommandType.TOUCH;
    }
}
