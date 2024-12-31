package io.github.fherbreteau.functional.domain.entities;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AbstractItem.AbstractBuilder;

public abstract class AbstractItem<T extends Item, B extends AbstractBuilder<T, B>> implements Item {

    private final UUID handle;

    private final String name;

    private final User owner;

    private final Group group;

    private final AccessRight ownerAccess;

    private final AccessRight groupAccess;

    private final AccessRight otherAccess;

    private final LocalDateTime created;

    private final LocalDateTime lastModified;

    private final LocalDateTime lastAccessed;

    private final Folder parent;

    protected AbstractItem(AbstractBuilder<T, B> builder) {
        this.handle = builder.handle;
        this.name = builder.name;
        this.owner = builder.owner;
        this.group = builder.group;
        this.ownerAccess = builder.ownerAccess;
        this.groupAccess = builder.groupAccess;
        this.otherAccess = builder.otherAccess;
        this.created = builder.created;
        this.lastModified = builder.lastModified;
        this.lastAccessed = builder.lastAccessed;
        this.parent = builder.parent;
    }

    @Override
    public UUID getHandle() {
        return handle;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public final User getOwner() {
        return owner;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public AccessRight getOwnerAccess() {
        return ownerAccess;
    }

    @Override
    public AccessRight getGroupAccess() {
        return groupAccess;
    }

    @Override
    public AccessRight getOtherAccess() {
        return otherAccess;
    }

    @Override
    public final LocalDateTime getCreated() {
        return created;
    }

    @Override
    public final LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public final LocalDateTime getLastAccessed() {
        return lastAccessed;
    }

    @Override
    public final Folder getParent() {
        return parent;
    }

    @Override
    public String getPath() {
        if (parent == null) {
            return name;
        }
        return String.format("%s/%s", parent.getPath(), name);
    }

    @Override
    public String toString() {
        return "'" + name +
                " " + owner + ":" + group +
                " " + ownerAccess + groupAccess + otherAccess +
                " " + (parent == null ? "null" : parent.getPath()) + "'";
    }

    protected B copy(B builder) {
        return builder
                .withName(name)
                .withHandle(handle)
                .withOwner(owner)
                .withGroup(group)
                .withOwnerAccess(ownerAccess)
                .withGroupAccess(groupAccess)
                .withOtherAccess(otherAccess)
                .withCreated(created)
                .withLastModified(lastModified)
                .withLastAccessed(lastAccessed)
                .withParent(parent);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractItem<?, ?> that)) {
            return false;
        }
        return Objects.equals(name, that.name)
                && Objects.equals(owner, that.owner)
                && Objects.equals(group, that.group)
                && Objects.equals(ownerAccess, that.ownerAccess)
                && Objects.equals(groupAccess, that.groupAccess)
                && Objects.equals(otherAccess, that.otherAccess)
                && Objects.equals(created, that.created)
                && Objects.equals(lastModified, that.lastModified)
                && Objects.equals(lastAccessed, that.lastAccessed)
                && Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, group, ownerAccess, groupAccess, otherAccess, created, lastModified, lastAccessed, parent);
    }

    @SuppressWarnings("unchecked")
    public abstract static class AbstractBuilder<I extends Item, B extends AbstractBuilder<I, B>> {

        private UUID handle;

        private String name;

        private User owner;

        private Group group;

        private AccessRight ownerAccess = AccessRight.none();

        private AccessRight groupAccess = AccessRight.none();

        private AccessRight otherAccess = AccessRight.none();

        private LocalDateTime created = LocalDateTime.now();

        private LocalDateTime lastModified = LocalDateTime.now();

        private LocalDateTime lastAccessed = LocalDateTime.now();

        private Folder parent;

        protected AbstractBuilder() {
        }

        public B withHandle(UUID handle) {
            this.handle = handle;
            return (B) this;
        }

        public B withName(String name) {
            this.name = requireNonNull(name);
            return (B) this;
        }

        public B withOwner(User owner) {
            this.owner = requireNonNull(owner);
            if (group == null) {
                this.group = owner.getGroup();
            }
            return (B) this;
        }

        public B withGroup(Group group) {
            this.group = requireNonNull(group);
            return (B) this;
        }

        public B withOwnerAccess(AccessRight access) {
            this.ownerAccess = requireNonNull(access);
            return (B) this;
        }

        public B withGroupAccess(AccessRight access) {
            this.groupAccess = requireNonNull(access);
            return (B) this;
        }

        public B withOtherAccess(AccessRight access) {
            this.otherAccess = requireNonNull(access);
            return (B) this;
        }

        public B withCreated(LocalDateTime created) {
            this.created = requireNonNull(created);
            return (B) this;
        }

        public B withLastModified(LocalDateTime lastModified) {
            this.lastModified = requireNonNull(lastModified);
            return (B) this;
        }

        public B withLastAccessed(LocalDateTime lastAccessed) {
            this.lastAccessed = requireNonNull(lastAccessed);
            return (B) this;
        }

        public B withParent(Folder parent) {
            this.parent = parent;
            return (B) this;
        }

        protected void validate() {
            if (isNull(name)) {
                throw new IllegalArgumentException("name is required");
            }
            if (isNull(owner)) {
                throw new IllegalArgumentException("owner is required");
            }
        }

        public abstract I build();
    }
}
