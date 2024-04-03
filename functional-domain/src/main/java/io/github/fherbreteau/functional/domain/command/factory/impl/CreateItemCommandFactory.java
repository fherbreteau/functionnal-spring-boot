package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.CreateFileCommand;
import io.github.fherbreteau.functional.domain.command.impl.CreateFolderCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

@SuppressWarnings({"rawtypes"})
public class CreateItemCommandFactory implements CommandFactory {

    @Override
    public boolean supports(CommandType commandType, Input input) {
        return (commandType == CommandType.TOUCH || commandType == CommandType.MKDIR) && isValid(input);
    }

    private boolean isValid(Input input) {
        return input.getItem() instanceof Folder && input.getName() != null;
    }

    @Override
    public Command createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        if (type == CommandType.TOUCH) {
            return new CreateFileCommand(repository, accessChecker, input.getName(), (Folder) input.getItem());
        }
        return new CreateFolderCommand(repository, accessChecker, input.getName(), (Folder) input.getItem());
    }
}
