package io.github.fherbreteau.functional.domain.entities;

import java.util.List;
import java.util.UUID;

public final class UserInput {

    private final UUID userId;
    private final String name;
    private final String password;
    private final UUID groupId;
    private final List<String> groups;
    private final String newName;
    private final boolean force;
    private final boolean append;

    private UserInput(Builder builder) {
        userId = builder.userId;
        name = builder.name;
        password = builder.password;
        groupId = builder.groupId;
        groups = builder.groups;
        newName = builder.newName;
        force = builder.force;
        append = builder.append;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public List<String> getGroups() {
        return groups;
    }

    public String getNewName() {
        return newName;
    }

    public boolean isForce() {
        return force;
    }

    public boolean isAppend() {
        return append;
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", groupId=" + groupId +
                ", groups='" + groups + '\'' +
                ", newName='" + newName + '\'' +
                ", force=" + force +
                ", append=" + append +
                '}';
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private UUID userId;
        private String password;
        private UUID groupId;
        private List<String> groups = List.of();
        private String newName;
        private boolean force;
        private boolean append;

        private Builder(String name) {
            this.name = name;
        }

        public Builder withUserId(UUID uuid) {
            userId = uuid;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withGroupId(UUID uuid) {
            groupId = uuid;
            return this;
        }

        public Builder withGroups(List<String> names) {
            groups = names;
            return this;
        }

        public Builder withNewName(String name) {
            newName = name;
            return this;
        }

        public Builder withForce(boolean force) {
            this.force = force;
            return this;
        }

        public Builder withAppend(boolean append) {
            this.append = append;
            return this;
        }

        public UserInput build() {
            return new UserInput(this);
        }
    }
}
