package io.github.fherbreteau.functional.domain.entities;

import java.util.UUID;

public final class Folder extends AbstractItem<Folder, Folder.Builder> {

    private static final UUID ROOT_HANDLE = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private static final Folder ROOT = builder()
            .withHandle(ROOT_HANDLE)
            .withName("")
            .withOwner(User.root())
            .withGroup(Group.root())
            .withOtherAccess(AccessRight.full())
            .build();

    private Folder(Builder builder) {
        super(builder);
    }

    public static Folder getRoot() {
        return ROOT;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public Builder copyBuilder() {
        return copy(builder());
    }

    public static final class Builder extends AbstractBuilder<Folder, Builder> {

        private Builder() { }

        @Override
        public Folder build() {
            validate();
            return new Folder(this);
        }
    }
}
