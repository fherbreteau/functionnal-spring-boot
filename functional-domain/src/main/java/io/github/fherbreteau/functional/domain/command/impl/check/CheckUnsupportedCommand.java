package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckUnsupportedCommand extends AbstractCheckCommand<ErrorCommand> {

    private final CommandType commandType;

    private final Input input;

    public CheckUnsupportedCommand(FileRepository repository, AccessChecker accessChecker, CommandType commandType, Input input) {
        super(repository, accessChecker);
        this.commandType = commandType;
        this.input = input;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return true;
    }

    @Override
    protected ErrorCommand createSuccess() {
        return new ErrorCommand(commandType, input);
    }
}
