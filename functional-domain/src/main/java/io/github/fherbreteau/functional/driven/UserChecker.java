package io.github.fherbreteau.functional.driven;

import io.github.fherbreteau.functional.domain.entities.User;

public interface UserChecker {

    boolean canCreateUser(String name, User actor);

    boolean canUpdateUser(String name, User actor);

    boolean canDeleteUser(String name, User actor);

    boolean canCreateGroup(String name, User actor);

    boolean canUpdateGroup(String name, User actor);

    boolean canDeleteGroup(String name, User actor);
}
