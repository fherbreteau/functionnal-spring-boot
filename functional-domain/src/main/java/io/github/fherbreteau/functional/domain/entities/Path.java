package io.github.fherbreteau.functional.domain.entities;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Path {

    public static final Path ROOT = success(Folder.getRoot());

    private final Item item;

    private final Failure failure;

    private Path(Item item, Failure failure) {
        this.item = item;
        this.failure = failure;
    }

    public static Path success(Item item) {
        return new Path(item, null);
    }

    public static Path error(Failure failure) {
        return new Path(null, failure);
    }

    public String getName() {
        return item.getName();
    }

    public Item getItem() {
        return item;
    }

    public boolean isItemFolder() {
        return item.isFolder();
    }

    public Folder getAsFolder() {
        return (Folder) item;
    }

    public boolean isItemFile() {
        return item.isFile();
    }

    public File getAsFile() {
        return (File) item;
    }

    public Failure getError() {
        return failure;
    }

    public boolean isError() {
        return failure != null;
    }

    public Path getParent() {
        if (hasParent()) {
            return Path.success(item.getParent());
        }
        if (ROOT.equals(this)) {
            return Path.error(Failure.failure("Root path has no parent", List.of()));
        }
        return this;
    }

    public boolean hasParent() {
        return item != null && item.getParent() != null;
    }

    public String getContentType() {
        return isItemFolder() ? null : getAsFile().getContentType();
    }

    public UUID getHandle() {
        return item.getHandle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Path path)) {
            return false;
        }
        if (isError()) {
            return path.isError() && Objects.equals(failure, path.failure);
        }
        return !path.isError() && Objects.equals(item, path.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, failure);
    }

    @Override
    public String toString() {
        String result = "Path{";
        if (item != null) {
            result += "item=" + item;
        } else {
            result += "error=" + failure;
        }
        return result + '}';
    }
}
