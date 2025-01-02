package io.github.fherbreteau.functional.domain.entities;

import java.util.UUID;

public final class FolderItem extends AbstractItem<Folder, FolderItem.Builder> implements Folder {

    private static final UUID ROOT_HANDLE = UUID.fromString("00000000-0000-0000-0000-000000000000");

    static final Folder ROOT = Folder.builder()
            .withHandle(ROOT_HANDLE)
            .withName("")
            .withOwner(User.root())
            .withGroup(Group.root())
            .withOtherAccess(AccessRight.full())
            .build();

    private FolderItem(Builder builder) {
        super(builder);
    }

    @Override
    public boolean isFolder() {
        return true;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Builder copyBuilder() {
        return new Builder(this);
    }

    public static final class Builder extends AbstractBuilder<Folder, Builder> {

        Builder() {
        }

        Builder(Folder folder) {
            super(folder);
        }

        @Override
        public Folder build() {
            validate();
            return new FolderItem(this);
        }
    }
}
