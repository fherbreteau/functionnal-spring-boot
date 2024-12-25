package io.github.fherbreteau.functional.domain.entities;

import io.github.fherbreteau.functional.domain.entities.FileItem.Builder;

public interface File extends Item {

    String TYPE = "File";

    static Builder builder() {
        return new Builder();
    }

    String getContentType();

    @SuppressWarnings("unchecked")
    Builder copyBuilder();
}
