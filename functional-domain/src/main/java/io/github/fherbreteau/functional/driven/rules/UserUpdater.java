package io.github.fherbreteau.functional.driven.rules;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;

public interface UserUpdater {

    User createUser(User user);

    User updateUser(User oldUser, User newUser);

    void deleteUser(User user);

    Group createGroup(Group group);

    Group updateGroup(Group oldGroup, Group newGroup);

    void deleteGroup(Group group);
}
