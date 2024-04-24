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

    public boolean isRead() {
        return read;
    }

    public boolean isWrite() {
        return write;
    }

    public boolean isExecute() {
        return execute;
    }

    public AccessRight read() {
        return new AccessRight(true, this.write, this.execute);
    }

    public AccessRight write() {
        return new AccessRight(this.read, true, this.execute);
    }

    public AccessRight execute() {
        return new AccessRight(this.read, this.write, true);
    }

    public static AccessRight readOnly() {
        return none().read();
    }

    public static AccessRight readWrite() {
        return readOnly().write();
    }

    public static AccessRight readExecute() {
        return readOnly().execute();
    }

    public static AccessRight writeOnly() {
        return none().write();
    }

    public static AccessRight writeExecute() {
        return writeOnly().execute();
    }

    public static AccessRight executeOnly() {
        return none().execute();
    }

    public static AccessRight full() {
        return readWrite().execute();
    }

    public static AccessRight none() {
        return new AccessRight(false, false, false);
    }

    @Override
    public String toString() {
        return (read ? "r" : "-") +
                (write ? "w" : "-") +
                (execute ? "x" : "-");
    }
}
