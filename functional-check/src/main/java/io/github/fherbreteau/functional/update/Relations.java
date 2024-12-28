package io.github.fherbreteau.functional.update;

public final class Relations {
    private Relations() { }

    public static final String ANYONE_ID = "-1";

    // User, Group, Anyone relations.
    public static final String USER_READ = "user_read";
    public static final String USER_WRITE = "user_write";
    public static final String USER_EXECUTE = "user_execute";
    public static final String GROUP_READ = "group_read";
    public static final String GROUP_WRITE = "group_write";
    public static final String GROUP_EXECUTE = "group_execute";
    public static final String ANYONE_READ = "anyone_read";
    public static final String ANYONE_WRITE = "anyone_write";
    public static final String ANYONE_EXECUTE = "anyone_execute";
    // User / group relations.
    public static final String MEMBER = "member";
    public static final String SUPER_USER = "super_user";
}
