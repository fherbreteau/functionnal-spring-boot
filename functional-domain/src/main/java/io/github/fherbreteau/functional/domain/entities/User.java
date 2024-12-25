package io.github.fherbreteau.functional.domain.entities;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class User {

    private static final UUID ROOT = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private final UUID userId;

    private final String name;

    private final List<Group> groups;

    private User(Builder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.groups = builder.groups;
    }

    public static Builder builder(String name) {
        return new Builder().withName(name);
    }

    public static User root() {
        return User.builder("root")
                .withUserId(ROOT)
                .withGroup(Group.root())
                .build();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return groups.get(0);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public boolean isSuperUser() {
        return ROOT.equals(userId);
    }

    public Builder copy() {
        return new Builder(this);
    }

    @Override
    public String toString() {
        return getName() + "(" + userId + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name) && Objects.equals(groups, user.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, groups);
    }

    public static final class Builder {
        private String name;
        private UUID userId;
        private List<Group> groups;

        private Builder() {
        }

        private Builder(User user) {
            name = user.name;
            userId = user.userId;
            groups = List.copyOf(user.groups);
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withUserId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder withGroup(Group group) {
            List<Group> newGroups = copyGroups();
            newGroups.remove(group);
            if (newGroups.isEmpty()) {
                newGroups.add(group);
            } else {
                newGroups.set(0, group);
            }
            return withGroups(List.copyOf(newGroups));
        }

        public Builder withGroups(List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Builder addGroups(List<Group> groups) {
            List<Group> newGroups = copyGroups();
            newGroups.addAll(groups.stream().filter(g -> !newGroups.contains(g)).toList());
            this.groups = List.copyOf(newGroups);
            return this;
        }

        private List<Group> copyGroups() {
            return this.groups != null ? new ArrayList<>(groups) : new ArrayList<>();
        }

        public User build() {
            if (isNull(userId)) {
                userId = UUID.randomUUID();
            }
            if (isNull(name)) {
                throw new NullPointerException("name is required");
            }
            if (name.isEmpty()) {
                throw new IllegalStateException("name must not be empty");
            }
            if (isNull(groups)) {
                groups = List.of(Group.builder(name).withGroupId(userId).build());
            }
            if (groups.isEmpty()) {
                throw new IllegalStateException("groups must contain at least one group");
            }
            return new User(this);
        }
    }
}
