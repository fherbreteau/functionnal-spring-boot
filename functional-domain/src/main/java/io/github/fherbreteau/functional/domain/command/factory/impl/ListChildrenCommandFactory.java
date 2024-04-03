package io.github.fherbreteau.functional.domain.command.factory.impl;

import io.github.fherbreteau.functional.domain.command.Command;
import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.factory.CommandFactory;
import io.github.fherbreteau.functional.domain.command.impl.ListChildrenCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.List;

@SuppressWarnings("rawtypes")
public class ListChildrenCommandFactory implements CommandFactory<List<Item>> {
    @Override
    public boolean supports(CommandType type, Input input) {
        return type == CommandType.LIST && input.getItem() instanceof Folder;
    }

    @Override
    public Command<List<Item>> createCommand(FileRepository repository, AccessChecker accessChecker, CommandType type, Input input) {
        return new ListChildrenCommand(repository, accessChecker, (Folder) input.getItem());
    }
}
