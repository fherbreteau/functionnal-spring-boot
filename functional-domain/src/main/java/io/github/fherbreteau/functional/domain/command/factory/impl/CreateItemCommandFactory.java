package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckCreateFileCommand;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckCreateFolderCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CreateItemCommandFactory implements CommandFactory {

    @Override
    public boolean supports(CommandType commandType, Input input) {
        return (commandType == CommandType.TOUCH || commandType == CommandType.MKDIR) && isValid(input);
    }

    private boolean isValid(Input input) {
        return input.getItem() instanceof Folder && input.getName() != null;
    }

    @Override
    public Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker,
                                                  ContentRepository contentRepository, CommandType type, Input input) {
        if (type == CommandType.TOUCH) {
            return new CheckCreateFileCommand(repository, accessChecker, input.getName(), (Folder) input.getItem());
        }
        return new CheckCreateFolderCommand(repository, accessChecker, input.getName(), (Folder) input.getItem());
    }
}
