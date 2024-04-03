package io.github.fherbreteau.functional.domain.entities;

public final class Folder extends AbstractItem<Folder, Folder.Builder> {

    private static final Folder ROOT = builder().withName("").withOtherAccess(AccessRight.full()).build();

    private Folder(Builder builder) {
        super(builder);
    }

    public static Item<?, ?> getRoot() {
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

    @Override
    public Builder copyBuilder() {
        return copy(builder());
    }

    public static final class Builder extends AbstractBuilder<Folder, Builder> {

        private Builder() {
        }

        @Override
        public Folder build() {
            return new Folder(this);
        }
    }
}
