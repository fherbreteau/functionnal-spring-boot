package io.github.fherbreteau.functional.domain.command.impl;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.List;

@SuppressWarnings("rawtypes")
public class ListChildrenCommand extends AbstractCommand<List<Item>> {

    private final Folder folder;

    public ListChildrenCommand(FileRepository repository, AccessChecker accessChecker, Folder folder) {
        super(repository, accessChecker);
        this.folder = folder;
    }

    @Override
    public boolean canExecute(User actor) {
        return accessChecker.canRead(folder, actor);
    }

    @Override
    public List<Item> execute(User actor) {
        return repository.findByParentAndUser(folder, actor);
    }

    @Override
    public Error handleError(User actor) {
        Input input = Input.builder(folder).build();
        return new Error(CommandType.LIST, input, actor);
    }

}
