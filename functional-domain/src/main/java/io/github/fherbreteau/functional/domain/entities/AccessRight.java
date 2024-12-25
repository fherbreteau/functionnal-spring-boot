package io.github.fherbreteau.functional.domain.entities;

import java.util.Objects;

public final class AccessRight {

    private final boolean read;

    private final boolean write;

    private final boolean execute;

    private AccessRight(boolean read, boolean write, boolean execute) {
        this.read = read;
        this.write = write;
        this.execute = execute;
    }

    public static AccessRight readOnly() {
        return none().addRead();
    }

    public static AccessRight readWrite() {
        return readOnly().addWrite();
    }

    public static AccessRight readExecute() {
        return readOnly().addExecute();
    }

    public static AccessRight writeOnly() {
        return none().addWrite();
    }

    public static AccessRight writeExecute() {
        return writeOnly().addExecute();
    }

    public static AccessRight executeOnly() {
        return none().addExecute();
    }

    public static AccessRight full() {
        return readWrite().addExecute();
    }

    public static AccessRight none() {
        return new AccessRight(false, false, false);
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

    public AccessRight addRead() {
        return new AccessRight(true, this.write, this.execute);
    }

    public AccessRight removeRead() {
        return new AccessRight(false, write, execute);
    }

    public AccessRight addWrite() {
        return new AccessRight(this.read, true, this.execute);
    }

    public AccessRight removeWrite() {
        return new AccessRight(read, false, execute);
    }

    public AccessRight addExecute() {
        return new AccessRight(this.read, this.write, true);
    }

    public AccessRight removeExecute() {
        return new AccessRight(read, write, false);
    }

    public AccessRight add(AccessRight accessRight) {
        AccessRight newAccessRight = accessRight;
        if (read) {
            newAccessRight = newAccessRight.addRead();
        }
        if (write) {
            newAccessRight = newAccessRight.addWrite();
        }
        if (execute) {
            newAccessRight = newAccessRight.addExecute();
        }
        return newAccessRight;
    }

    public AccessRight remove(AccessRight accessRight) {
        AccessRight newAccessRight = accessRight;
        if (read) {
            newAccessRight = newAccessRight.removeRead();
        }
        if (write) {
            newAccessRight = newAccessRight.removeWrite();
        }
        if (execute) {
            newAccessRight = newAccessRight.removeExecute();
        }
        return newAccessRight;
    }

    @Override
    public String toString() {
        return (read ? "r" : "-") +
                (write ? "w" : "-") +
                (execute ? "x" : "-");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessRight that)) {
            return false;
        }
        return read == that.read && write == that.write && execute == that.execute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(read, write, execute);
    }
}
