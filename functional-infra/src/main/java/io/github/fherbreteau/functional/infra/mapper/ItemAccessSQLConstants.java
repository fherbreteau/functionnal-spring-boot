package io.github.fherbreteau.functional.infra.mapper;

public final class ItemAccessSQLConstants {

    private ItemAccessSQLConstants() { }

    public static final String COL_ITEM_ID = "item_id";
    public static final String COL_TYPE = "type";
    public static final String COL_ATTRIBUTION = "attribution";
    public static final String COL_VALUE = "value";

    public static final String TYPE_READ = "READ";
    public static final String TYPE_WRITE = "WRITE";
    public static final String TYPE_EXECUTE = "EXECUTE";

    public static final String ATTR_OWNER = "OWNER";
    public static final String ATTR_GROUP = "GROUP";
    public static final String ATTR_OTHER = "OTHER";
}
