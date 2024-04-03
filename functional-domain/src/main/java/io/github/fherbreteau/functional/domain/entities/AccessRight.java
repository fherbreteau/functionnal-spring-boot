package io.github.fherbreteau.functional.domain.entities;

public final class AccessRight {

    private final boolean read;

    private final boolean write;

    private final boolean execute;

    private AccessRight(boolean read, boolean write, boolean execute) {
        this.read = read;
        this.write = write;
        this.execute = execute;
    }

    public static AccessRight read(boolean read) {
        return readWrite(read, false);
    }

    public static AccessRight write(boolean write) {
        return readWrite(false, write);
    }

    public static AccessRight execute(boolean execute) {
        return accessRight(false, false, execute);
    }

    public static AccessRight readWrite(boolean read, boolean write) {
        return accessRight(read, write, false);
    }

    public static AccessRight accessRight(boolean read, boolean write, boolean execute) {
        return new AccessRight(read, write, execute);
    }

    public static AccessRight full() {
        return accessRight(true, true, true);
    }

    public static AccessRight none() {
        return accessRight(false, false, false);
    }

    @Override
    public String toString() {
        return (read ? "r" : "-") +
                (write ? "w" : "-") +
                (execute ? "x" : "-");
    }
}
