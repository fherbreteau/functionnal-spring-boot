package io.github.fherbreteau.functional.domain.entities;

import io.github.fherbreteau.functional.domain.entities.FolderItem.Builder;

public interface Folder extends Item {

    String TYPE = "Folder";

    static Builder builder() {
        return new Builder();
    }

    static Folder getRoot() {
        return FolderItem.ROOT;
    }

    @SuppressWarnings("unchecked")
    Builder copyBuilder();
}
