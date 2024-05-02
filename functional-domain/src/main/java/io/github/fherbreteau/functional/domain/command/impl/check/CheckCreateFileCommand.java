package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.command.impl.success.CreateFileCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckCreateFileCommand extends AbstractCheckCommand<CreateFileCommand> {

    private final String name;
    private final Folder parent;

    public CheckCreateFileCommand(FileRepository repository, AccessChecker accessChecker, String name, Folder parent) {
        super(repository, accessChecker);
        this.name = name;
        this.parent = parent;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canWrite(parent, actor) && !repository.exists(parent, name);
    }

    @Override
    protected CreateFileCommand createSuccess() {
        return new CreateFileCommand(repository, name, parent);
    }

    @Override
    protected ErrorCommand createError() {
        Input input = Input.builder(parent)
                .withName(name)
                .build();
        return new ErrorCommand(CommandType.TOUCH, input);
    }
}
