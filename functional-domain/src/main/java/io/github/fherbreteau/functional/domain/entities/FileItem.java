package io.github.fherbreteau.functional.domain.entities;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public final class FileItem extends AbstractItem<File, FileItem.Builder> implements File {

    private final String contentType;

    private FileItem(Builder builder) {
        super(builder);
        this.contentType = builder.contentType;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Builder copyBuilder() {
        return copy(File.builder())
                .withContentType(contentType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileItem fileItem)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(contentType, fileItem.contentType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(contentType);
        return result;
    }

    public static final class Builder extends AbstractBuilder<File, Builder> {

        private String contentType = "application/octet-stream";

        Builder() {
        }

        public Builder withContentType(String contentType) {
            this.contentType = requireNonNull(contentType);
            return this;
        }

        @Override
        public File build() {
            validate();
            return new FileItem(this);
        }
    }
}
