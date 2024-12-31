package io.github.fherbreteau.functional.domain.command.impl.check;

import static java.lang.System.Logger.Level.DEBUG;

import java.util.ArrayList;
import java.util.List;

import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ListChildrenCommand;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;

public class CheckListChildrenCommand extends AbstractCheckItemCommand<List<Item>, ListChildrenCommand> {

    private final Folder item;

    public CheckListChildrenCommand(ItemRepository repository, AccessChecker accessChecker, Folder item) {
        super(repository, accessChecker);
        this.item = item;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canRead(item, actor)) {
            reasons.add(String.format("%s can't read %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected ListChildrenCommand createSuccess() {
        logger.log(DEBUG, "Creating execute command");
        return new ListChildrenCommand(repository, item);
    }

    @Override
    protected ItemErrorCommand<List<Item>> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item).build();
        return new ItemErrorCommand<>(ItemCommandType.LIST, itemInput, reasons);
    }
}
