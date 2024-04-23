package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.command.Output;
import io.github.fherbreteau.functional.domain.entities.AbstractItem.AbstractBuilder;
import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;

@SuppressWarnings({"rawtypes"})
public class ChangeModeCommand extends AbstractSuccessCommand {

    private final Item item;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    public ChangeModeCommand(FileRepository repository, Item item, AccessRight ownerAccess,
                             AccessRight groupAccess, AccessRight otherAccess) {
        super(repository);
        this.item = item;
        this.ownerAccess = ownerAccess;
        this.groupAccess = groupAccess;
        this.otherAccess = otherAccess;
    }

    @Override
    public Output execute(User actor) {
        AbstractBuilder builder = item.copyBuilder();
        if (ownerAccess != null) {
            builder.withOwnerAccess(ownerAccess);
        }
        if (groupAccess != null) {
            builder.withGroupAccess(groupAccess);
        }
        if (otherAccess != null) {
            builder.withOtherAccess(otherAccess);
        }
        return new Output(repository.save(builder.build()));
    }
}
