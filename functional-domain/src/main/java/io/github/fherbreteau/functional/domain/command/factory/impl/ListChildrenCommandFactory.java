package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.entities.CommandType;
import io.github.fherbreteau.functional.domain.entities.Input;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.check.CheckListChildrenCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class ListChildrenCommandFactory implements CommandFactory {
    @Override
    public boolean supports(CommandType type, Input input) {
        return type == CommandType.LIST && input.getItem() instanceof Folder;
    }

    @Override
    public Command<Command<Output>> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new CheckListChildrenCommand(repository, accessChecker, (Folder) input.getItem());
    }
}
