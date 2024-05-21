package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.success.CreateFolderCommand;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.ArrayList;
import java.util.List;

public class CheckCreateFolderCommand extends AbstractCheckItemCommand<CreateFolderCommand> {
    private final String name;
    private final Folder parent;

    public CheckCreateFolderCommand(FileRepository repository, AccessChecker accessChecker, String name, Folder parent) {
        super(repository, accessChecker);
        this.name = name;
        this.parent = parent;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canWrite(parent, actor)) {
            reasons.add(String.format("%s can't create folder in %s", actor, parent));
        }
        if (repository.exists(parent, name)) {
            reasons.add(String.format("%s already exists in  %s", name, parent));
        }
        return reasons;
    }

    @Override
    protected CreateFolderCommand createSuccess() {
        return new CreateFolderCommand(repository, name, parent);
    }

    @Override
    protected ItemErrorCommand createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(parent)
                .withName(name)
                .build();
        return new ItemErrorCommand(ItemCommandType.MKDIR, itemInput, reasons);
    }
}
