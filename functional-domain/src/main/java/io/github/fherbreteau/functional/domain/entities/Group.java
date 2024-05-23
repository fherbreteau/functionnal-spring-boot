package io.github.fherbreteau.functional.domain.entities;

import java.util.Objects;
import java.util.UUID;

public final class Group {

    private static final UUID ROOT = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final UUID groupId;

    private final String name;

    private Group(Builder builder) {
        groupId = builder.groupId;
        name = builder.name;
    }

    public static Group root() {
        return builder("root").withGroupId(ROOT).build();
    }

    public UUID getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public boolean isRoot() {
        return Objects.equals(ROOT, groupId);
    }

    public Group withGroupId(UUID groupId) {
        return Group.builder(name).withGroupId(groupId).build();
    }

    public Group withName(String name) {
        return Group.builder(name).withGroupId(groupId).build();
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
                && Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name);
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private UUID groupId;

        private Builder(String name) {
            this.name = name;
        }

        public Builder withGroupId(UUID groupId) {
            this.groupId = groupId;
            return this;
        }

        public Group build() {
            if (Objects.isNull(groupId)) {
                groupId = UUID.randomUUID();
            }
            if (Objects.isNull(name)) {
                throw new NullPointerException("name is required");
            }
            if (name.isEmpty()) {
                throw new IllegalStateException("name must not be empty");
            }
            return new Group(this);
        }
    }
}
