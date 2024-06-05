package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.DeleteItemCommand;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class CheckDeleteItemCommand extends AbstractCheckItemCommand<Void, DeleteItemCommand> {
    private final ContentRepository contentRepository;
    private final AccessUpdater accessUpdater;
    private final Item item;

    public CheckDeleteItemCommand(ItemRepository repository, ContentRepository contentRepository,
                                  AccessChecker accessChecker, AccessUpdater accessUpdater, Item item) {
        super(repository, accessChecker);
        this.contentRepository = contentRepository;
        this.accessUpdater = accessUpdater;
        this.item = item;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canWrite(item.getParent(), actor)) {
            reasons.add(String.format("%s can't delete %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected DeleteItemCommand createSuccess() {
        return new DeleteItemCommand(repository, contentRepository, accessUpdater, item);
    }

    @Override
    protected final ItemErrorCommand<Void> createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item).build();
        return new ItemErrorCommand<>(ItemCommandType.DELETE, itemInput, reasons);
    }
}
