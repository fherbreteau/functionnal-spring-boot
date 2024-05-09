package io.github.fherbreteau.functional.domain.entities;

import java.util.Arrays;

public final class File extends AbstractItem<File, File.Builder> {

    private final byte[] content;

    private File(Builder builder) {
        super(builder);
        this.content = builder.content;
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

    public byte[] getContent() {
        return content;
    }

    @SuppressWarnings("unchecked")
    public Builder copyBuilder() {
        return copy(builder())
                .withContent(content);
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
        return Arrays.equals(content, file.content);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    public static final class Builder extends AbstractBuilder<File, Builder> {

        private byte[] content = new byte[0];

        private Builder() { }

        public Builder withContent(byte[] content) {
            this.content = content != null ? Arrays.copyOf(content, content.length) : null;
            return this;
        }

        @Override
        public File build() {
            return new File(this);
        }
    }
}
