package io.github.fherbreteau.functional.domain.entities;

import java.util.UUID;

public final class UserInput {

    private final UUID userId;
    private final String name;
    private final String password;
    private final UUID groupId;
    private final String groupName;
    private final String newName;
    private final boolean force;

    private UserInput(Builder builder) {
        userId = builder.userId;
        name = builder.name;
        password = builder.password;
        groupId = builder.groupId;
        groupName = builder.groupName;
        newName = builder.newName;
        force = builder.force;
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

    public String getGroupName() {
        return groupName;
    }

    public String getNewName() {
        return newName;
    }

    public boolean isForce() {
        return force;
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", newName='" + newName + '\'' +
                ", force=" + force +
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
        private String groupName;
        private String newName;
        private boolean force;

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

        public Builder withGroupName(String name) {
            groupName = name;
            return this;
        }

        public Builder withNewName(String newName) {
            this.newName = newName;
            return this;
        }

        public Builder withForce(boolean force) {
            this.force = force;
            return this;
        }

        public UserInput build() {
            return new UserInput(this);
        }
    }
}
