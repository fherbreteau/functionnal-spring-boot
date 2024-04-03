package io.github.fherbreteau.functional.domain.entities;

import java.util.UUID;

public class User {

    private static final UUID ROOT = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final UUID userId;

    private final Group group;

    private User(UUID userId, Group group) {
        this.userId = userId;
        this.group = group;
    }

    public UUID getUserId() {
        return userId;
    }

    public Group getGroup() {
        return group;
    }

    public boolean isSuperUser() {
        return ROOT.equals(userId);
    }

    public static User user(UUID userId) {
        return new User(userId, Group.group(userId));
    }

    public static User user(UUID userId, UUID groupId) {
        return new User(userId, Group.group(groupId));
    }

    public static User user(UUID userId, Group group) {
        return new User(userId, group);
    }
}
