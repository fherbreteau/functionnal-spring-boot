package io.github.fherbreteau.functional.domain.entities;

import java.util.Objects;
import java.util.UUID;

public final class Group {

    private static final UUID ROOT = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final UUID groupId;

    private final String name;

    private final Group parent;

    private Group(UUID groupId, String name, Group parent) {
        this.groupId = groupId;
        this.name = name;
        this.parent = parent;
    }

    public static Group root() {
        return group(ROOT, "root", null);
    }

    public static Group group(String name) {
        return group(UUID.randomUUID(), name);
    }

    public static Group group(UUID groupId, String name) {
        return group(groupId, name, root());
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
        return getName() + "(" + groupId + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group group)) {
            return false;
        }
        return Objects.equals(groupId, group.groupId)
                && Objects.equals(name, group.name)
                && Objects.equals(parent, group.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name, parent);
    }
}
