package io.github.fherbreteau.functional.check;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.rules.AccessChecker;
import org.springframework.stereotype.Service;

@Service
public class AccessCheckerImpl implements AccessChecker {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";

    @Override
    public <T extends Item> boolean canRead(T item, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <T extends Item> boolean canWrite(T item, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <T extends Item> boolean canExecute(T item, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <T extends Item> boolean canChangeMode(T item, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <T extends Item> boolean canChangeOwner(T item, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <T extends Item> boolean canChangeGroup(T item, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
