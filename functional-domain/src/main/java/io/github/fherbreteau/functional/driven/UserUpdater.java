package io.github.fherbreteau.functional.driven;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;

public interface UserUpdater {

    User createUser(User user);

    User updateUser(User oldUser, User newUser);

    User deleteUser(User user);

    Group createGroup(Group group);

    Group updateGroup(Group oldGroup, Group newGroup);

    Group deleteGroup(Group group);
}
