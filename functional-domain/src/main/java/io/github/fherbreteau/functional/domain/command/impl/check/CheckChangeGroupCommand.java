package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.CommandType;
import io.github.fherbreteau.functional.domain.command.Input;
import io.github.fherbreteau.functional.domain.command.impl.error.ErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeGroupCommand;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CheckChangeGroupCommand extends AbstractCheckCommand<ChangeGroupCommand> {

    private final Item item;

    private final Group newGroup;

    public CheckChangeGroupCommand(FileRepository repository, AccessChecker accessChecker, Item item, Group newGroup) {
        super(repository, accessChecker);
        this.item = item;
        this.newGroup = newGroup;
    }

    @Override
    protected boolean checkAccess(User actor) {
        return accessChecker.canChangeGroup(item, actor);
    }

    @Override
    protected ChangeGroupCommand createSuccess() {
        return new ChangeGroupCommand(repository, item, newGroup);
    }

    @Override
    protected ErrorCommand createError() {
        Input input = Input.builder(item).withGroup(newGroup).build();
        return new ErrorCommand(CommandType.CHGRP, input);
    }
}
