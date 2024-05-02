package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckChangeOwnerCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeOwnerCommandFactory implements CommandFactory {
    @Override
    public boolean supports(CommandType commandType, Input input) {
        return commandType == CommandType.CHOWN && isValid(input);
    }

    private boolean isValid(Input input) {
        return input.getItem() != null && input.getUser() != null;
    }

    @Override
    public Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new CheckChangeOwnerCommand(repository, accessChecker, input.getItem(), input.getUser());
    }
}
