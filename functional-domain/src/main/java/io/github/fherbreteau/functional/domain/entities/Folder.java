package io.github.fherbreteau.functional.domain.entities;

public class Folder extends AbstractItem<Folder, Folder.Builder> {

    private static final Folder ROOT = builder().withName("").withOtherAccess(AccessRight.full()).build();

    protected Folder(Builder builder) {
        super(builder);
    }

    public static Item<?,?> getRoot() {
        return ROOT;
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
        return copy(new Builder());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractItem.Builder<Folder, Builder> {

        private Builder() {}

        @Override
        public Folder build() {
            return new Folder(this);
        }
    }
}
