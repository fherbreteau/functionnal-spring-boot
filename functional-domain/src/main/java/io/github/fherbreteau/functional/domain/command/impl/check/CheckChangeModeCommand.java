package io.github.fherbreteau.functional.domain.command.impl.check;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.command.impl.error.ItemErrorCommand;
import io.github.fherbreteau.functional.domain.command.impl.success.ChangeModeCommand;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessChecker;
import io.github.fherbreteau.functional.driven.FileRepository;

import java.util.ArrayList;
import java.util.List;

public class CheckChangeModeCommand extends AbstractCheckItemCommand<ChangeModeCommand> {

    private final Item item;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    public CheckChangeModeCommand(FileRepository repository, AccessChecker accessChecker, Item item,
                                     AccessRight ownerAccess, AccessRight groupAccess, AccessRight otherAccess) {
        super(repository, accessChecker);
        this.item = item;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
    }

    @Override
    protected List<String> checkAccess(User actor) {
        List<String> reasons = new ArrayList<>();
        if (!accessChecker.canChangeMode(item, actor)) {
            reasons.add(String.format("%s can't change mode of %s", actor, item));
        }
        return reasons;
    }

    @Override
    protected ChangeModeCommand createSuccess() {
        return new ChangeModeCommand(repository, item, ownerAccess, groupAccess, otherAccess);
    }

    @Override
    protected ItemErrorCommand createError(List<String> reasons) {
        ItemInput itemInput = ItemInput.builder(item)
                .withOwnerAccess(ownerAccess)
                .withGroupAccess(groupAccess)
                .withOtherAccess(otherAccess)
                .build();
        return new ItemErrorCommand(ItemCommandType.CHMOD, itemInput, reasons);
    }
}
