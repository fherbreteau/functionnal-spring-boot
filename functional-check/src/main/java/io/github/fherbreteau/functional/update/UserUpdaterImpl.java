package io.github.fherbreteau.functional.update;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.UserUpdater;
import org.springframework.stereotype.Service;

@Service
public class UserUpdaterImpl implements UserUpdater {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";

    @Override
    public User createUser(User user) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public User updateUser(User oldUser, User newUser) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void deleteUser(User user) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Group createGroup(Group group) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public Group updateGroup(Group oldGroup, Group newGroup) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void deleteGroup(Group group) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
