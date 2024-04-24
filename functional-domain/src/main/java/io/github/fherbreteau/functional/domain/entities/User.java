package io.github.fherbreteau.functional.domain.entities;

import java.util.Objects;
import java.util.UUID;

public final class User {

    private static final UUID ROOT = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final UUID userId;

    private final String name;

    private final Group group;

    private User(UUID userId, String name, Group group) {
        this.userId = userId;
        this.name = name;
        this.group = group;
    }

    public static User root() {
        return user(ROOT, "root", Group.root());
    }

    public static User user(String name) {
        return user(UUID.randomUUID(), name);
    }

    public static User user(UUID userId, String name) {
        return user(userId, name, Group.group(userId, name));
    }

    public static User user(String name, Group group) {
        return user(UUID.randomUUID(), name, group);
    }

    public static User user(UUID userId, String name, Group group) {
        return new User(userId, name, group);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public boolean isSuperUser() {
        return ROOT.equals(userId);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name) && Objects.equals(group, user.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, group);
    }
}
