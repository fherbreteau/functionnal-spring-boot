package io.github.fherbreteau.functional.domain.path;

import io.github.fherbreteau.functional.domain.entities.Error;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;

public class Path {

    public static final Path ROOT = success(Item.ROOT, Folder.getRoot());

    private final String name;

    private final Item<?, ?> item;

    private final Error error;

    private Path(String name, Item<?, ?> item, Error error) {
        this.name = name;
        this.item = item;
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public Item getItem() {
        return item;
    }

    public Folder getItemAsFolder() {
        return (Folder) item;
    }

    public boolean isItemFolder() {
        return item instanceof Folder;
    }

    public Error getError() {
        return error;
    }

    public boolean isError() {
        return error != null;
    }

    public static Path success(String path, Item<?,?> item) {
        return new Path(path, item, null);
    }

    public static Path error(Error error) {
        return new Path(null, null, error);
    }
}
