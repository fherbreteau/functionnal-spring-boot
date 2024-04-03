package io.github.fherbreteau.functional.domain.entities;

import java.time.LocalDateTime;

public abstract class AbstractItem<T extends Item<T,B>, B extends AbstractItem.Builder<T, B>> implements Item<T, B> {

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


    protected AbstractItem(Builder<T, B> builder) {
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
        return null;
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
        if (parent == null)
            return name;
        return String.format("%s/%s", parent.getPath(), name);
    }

    @Override
    public String toString() {
        return "'" + name + " " + owner + ":" + group + " " + ownerAccess + groupAccess +otherAccess + " " + parent.getPath() + "'";
    }

    protected B copy(B builder) {
        return builder
                .withName(name)
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

    @SuppressWarnings("unchecked")
    public static abstract class Builder<I extends Item<I,B>, B extends Builder<I,B>> {

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

        protected Builder() {}

        public B withName(String name) {
            this.name = name;
            return (B) this;
        }

        public B withOwner(User owner) {
            this.owner = owner;
            if (group == null) {
                this.group = owner.getGroup();
            }
            return (B) this;
        }

        public B withGroup(Group group) {
            this.group = group;
            return (B) this;
        }

        public B withOwnerAccess(AccessRight access) {
            this.ownerAccess = access;
            return (B) this;
        }

        public B withGroupAccess(AccessRight access) {
            this.groupAccess = access;
            return (B) this;
        }

        public B withOtherAccess(AccessRight access) {
            this.otherAccess = access;
            return (B) this;
        }

        public B withCreated(LocalDateTime created) {
            this.created = created;
            return (B) this;
        }

        public B withLastModified(LocalDateTime lastModified) {
            this.lastModified = lastModified;
            return (B) this;
        }

        public B withLastAccessed(LocalDateTime lastAccessed) {
            this.lastAccessed = lastAccessed;
            return (B) this;
        }

        public B withParent(Folder parent) {
            this.parent = parent;
            return (B) this;
        }

        public abstract I build();
    }
}
