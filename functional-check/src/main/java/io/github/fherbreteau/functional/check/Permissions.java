package io.github.fherbreteau.functional.check;

public final class Permissions {

    private Permissions() { }

    // Item permissions
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String EXECUTE = "execute";
    public static final String CHANGE_MODE = "change_mode";
    public static final String CHANGE_OWNER = "change_owner";
    public static final String CHANGE_GROUP = "change_group";

    // User permissions
    public static final String CREATE_USER = "create-user";
    public static final String UPDATE_USER = "update-user";
    public static final String DELETE_USER = "delete-user";

    // Group permissions
    public static final String CREATE_GROUP = "create-group";
    public static final String UPDATE_GROUP = "update-group";
    public static final String DELETE_GROUP = "delete-group";
}
