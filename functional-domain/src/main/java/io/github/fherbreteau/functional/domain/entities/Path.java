package io.github.fherbreteau.functional.domain.entities;

import java.util.List;
import java.util.Objects;

public final class Path {

    public static final Path ROOT = success(Folder.getRoot());

    private final Item item;

    private final Error error;

    private Path(Item item, Error error) {
        this.item = item;
        this.error = error;
    }

    public static Path success(Item item) {
        return new Path(item, null);
    }

    public static Path error(Error error) {
        return new Path(null, error);
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

    public Error getError() {
        return error;
    }

    public boolean isError() {
        return error != null;
    }

    public Path getParent() {
        if (hasParent()) {
            return Path.success(item.getParent());
        }
        if (ROOT.equals(this)) {
            return Path.error(Error.error("Root path has no parent", List.of()));
        }
        return this;
    }

    public boolean hasParent() {
        return item != null && item.getParent() != null;
    }

    public String getContentType() {
        return isItemFolder() ? null : getAsFile().getContentType();
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
            return path.isError() && Objects.equals(error, path.error);
        }
        return !path.isError() && Objects.equals(item, path.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, error);
    }

    @Override
    public String toString() {
        String result = "Path{";
        if (item != null) {
            result += "item=" + item;
        } else {
            result += "error=" + error;
        }
        return result + '}';
    }
}
