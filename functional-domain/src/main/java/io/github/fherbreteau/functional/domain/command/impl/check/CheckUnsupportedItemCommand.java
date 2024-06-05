package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.util.List;

public class CheckUnsupportedItemCommand extends AbstractCheckItemCommand<Void, ItemErrorCommand<Void>> {

    private final ItemCommandType itemCommandType;

    private final ItemInput itemInput;

    public CheckUnsupportedItemCommand(ItemRepository repository, AccessChecker accessChecker,
                                       ItemCommandType itemCommandType, ItemInput itemInput) {
        super(repository, accessChecker);
        this.itemCommandType = itemCommandType;
        this.itemInput = itemInput;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        return List.of();
    }

    @Override
    protected ItemErrorCommand<Void> createSuccess() {
        return new ItemErrorCommand<>(itemCommandType, itemInput);
    }
}
