package io.github.fherbreteau.functional.rules.update;

public final class Relations {
    private Relations() { }

    public static final String OTHER_ID = "*";

    public static final String OWNER = "owner";
    public static final String OTHER = "other";

    // User, Group, Anyone relations.
    public static final String OWNER_READ = "owner_read";
    public static final String OWNER_WRITE = "owner_write";
    public static final String OWNER_EXECUTE = "owner_execute";
    public static final String GROUP_READ = "group_read";
    public static final String GROUP_WRITE = "group_write";
    public static final String GROUP_EXECUTE = "group_execute";
    public static final String OTHER_READ = "other_read";
    public static final String OTHER_WRITE = "other_write";
    public static final String OTHER_EXECUTE = "other_execute";
    // User / group relations.
    public static final String MEMBER = "member";
    public static final String ADMIN = "admin";
}
