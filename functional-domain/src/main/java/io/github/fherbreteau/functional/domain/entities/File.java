package io.github.fherbreteau.functional.domain.entities;

import java.util.Objects;

public final class File extends AbstractItem<File, File.Builder> {

    private final String contentType;

    private File(Builder builder) {
        super(builder);
        this.contentType = builder.contentType;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    public String getContentType() {
        return contentType;
    }

    @SuppressWarnings("unchecked")
    public Builder copyBuilder() {
        return copy(builder())
                .withContentType(contentType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File file)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(contentType, file.contentType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(contentType);
        return result;
    }

    public static final class Builder extends AbstractBuilder<File, Builder> {

        private String contentType = "application/octet-stream";

        private Builder() { }

        public Builder withContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        @Override
        public File build() {
            return new File(this);
        }
    }
}
