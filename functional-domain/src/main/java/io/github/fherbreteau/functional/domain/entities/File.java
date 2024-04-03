package io.github.fherbreteau.functional.domain.entities;

public class File extends AbstractItem<File, File.Builder> {

    private final byte[] content;

    private File(Builder builder) {
        super(builder);
        this.content = builder.content;
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
        return copy(new Builder())
                .withContent(content);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractItem.Builder<File, Builder> {

        private byte[] content = new byte[0];

        private Builder() {}

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
