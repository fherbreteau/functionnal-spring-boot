package io.github.fherbreteau.functional.domain.entities;

import java.util.UUID;

public final class Group {

    private final UUID groupId;

    private final String name;

    private final Group parent;

    private Group(UUID groupId, String name, Group parent) {
        this.groupId = groupId;
        this.name = name;
        this.parent = parent;
    }

    public static Group group(String name) {
        return group(UUID.randomUUID(), name);
    }

    public static Group group(UUID groupId, String name) {
        return group(groupId, name, null);
    }

    public static Group group(UUID groupId, String name, Group parent) {
        return new Group(groupId, name, parent);
    }

    public UUID getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public Group getParent() {
        return parent;
    }

    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
