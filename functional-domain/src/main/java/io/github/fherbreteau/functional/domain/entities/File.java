package io.github.fherbreteau.functional.domain.entities;

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

    @Override
    public Builder copyBuilder() {
        return copy(builder())
                .withContent(content);
    }

    public static final class Builder extends AbstractBuilder<File, Builder> {

        private byte[] content = new byte[0];

        private Builder() {
        }

        public Builder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        @Override
        public File build() {
            return new File(this);
        }
    }
}
