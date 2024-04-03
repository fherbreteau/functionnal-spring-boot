package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.ChangeGroupCommand;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ChangeGroupCommandFactory implements CommandFactory {
    @Override
    public boolean supports(CommandType commandType, Input input) {
        return commandType == CommandType.CHGRP && validInput(input);
    }

    private boolean validInput(Input input) {
        return input.getItem() != null && input.getGroup() != null;
    }

    @Override
    public Command createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new ChangeGroupCommand(repository, accessChecker, input.getItem(), input.getGroup());
    }
}
