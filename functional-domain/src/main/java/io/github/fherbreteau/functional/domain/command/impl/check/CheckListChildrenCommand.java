package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.impl.success.ListChildrenCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

public class CheckListChildrenCommand extends AbstractCheckCommand<ListChildrenCommand> {

    private final Folder item;

    public CheckListChildrenCommand(FileRepository repository, AccessChecker accessChecker, Folder item) {
        super(repository, accessChecker);
        this.item = item;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canRead(item, actor);
    }

    @Override
    protected ListChildrenCommand createSuccess() {
        return new ListChildrenCommand(repository, item);
    }

    @Override
    protected ErrorCommand createError() {
        Input input = Input.builder(item).build();
        return new ErrorCommand(CommandType.LIST, input);
    }
}
