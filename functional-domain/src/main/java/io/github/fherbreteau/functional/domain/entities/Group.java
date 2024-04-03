package io.github.fherbreteau.functional.domain.entities;

import java.util.UUID;

public class Group {

    private final UUID groupId;

    private final Group parent;

    private Group(UUID groupId, Group parent) {
        this.groupId = groupId;
        this.parent = parent;
    }

    public static Group group(UUID groupId) {
        return group(groupId, null);
    }

    public static Group group(UUID groupId, Group parent) {
        return new Group(groupId, parent);
    }

    public UUID getGroupId() {
        return groupId;
    }

    public Group getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }
}
