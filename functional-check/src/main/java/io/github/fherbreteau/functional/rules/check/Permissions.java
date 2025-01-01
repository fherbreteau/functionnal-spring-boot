package io.github.fherbreteau.functional.rules.check;

public final class Permissions {

    private Permissions() { }

    // Item permissions
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String EXECUTE = "execute";

    public static final String CHANGE_MODE = "change_mode";
    public static final String CHANGE_OWNER = "change_owner";
    public static final String CHANGE_GROUP = "change_group";

    // User & group permissions
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
}
