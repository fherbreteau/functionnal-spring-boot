package io.github.fherbreteau.functional.update;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.AccessUpdater;
import org.springframework.stereotype.Service;

@Service
public class AccessUpdaterImpl implements AccessUpdater {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";

    @Override
    public <I extends Item> I createItem(I item) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> I updateOwner(I item, User oldOwner) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> I updateGroup(I item, Group oldGroup) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> I updateOwnerAccess(I item, AccessRight oldOwner) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> I updateGroupAccess(I item, AccessRight oldGroup) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> I updateOtherAccess(I item, AccessRight oldOther) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> void deleteItem(I item) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
