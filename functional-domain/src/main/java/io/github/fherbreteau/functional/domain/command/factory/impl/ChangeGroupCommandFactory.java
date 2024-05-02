package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckChangeGroupCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ChangeGroupCommandFactory implements CommandFactory {
    @Override
    public boolean supports(CommandType commandType, Input input) {
        return commandType == CommandType.CHGRP && validInput(input);
    }

    private boolean validInput(Input input) {
        return input.getItem() != null && input.getGroup() != null;
    }

    @Override
    public Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new CheckChangeGroupCommand(repository, accessChecker, input.getItem(), input.getGroup());
    }
}
