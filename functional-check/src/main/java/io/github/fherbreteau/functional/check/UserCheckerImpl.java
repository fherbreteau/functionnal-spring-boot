package io.github.fherbreteau.functional.check;

import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.rules.UserChecker;
import org.springframework.stereotype.Service;

@Service
public class UserCheckerImpl implements UserChecker {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";

    @Override
    public boolean canCreateUser(String name, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean canUpdateUser(String name, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean canDeleteUser(String name, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean canCreateGroup(String name, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean canUpdateGroup(String name, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean canDeleteGroup(String name, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
