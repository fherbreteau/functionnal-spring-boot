package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.ATTR_GROUP;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.ATTR_OTHER;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.ATTR_OWNER;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.COL_ATTRIBUTION;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.COL_ITEM_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.COL_TYPE;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.COL_VALUE;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.TYPE_EXECUTE;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.TYPE_READ;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.TYPE_WRITE;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_ACCESSED_AT;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_CONTENT_TYPE;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_CREATED_AT;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_GROUP_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_ITEM_TYPE;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_MODIFIED_AT;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_NAME;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_OWNER_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_PARENT_ID;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Item;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class ItemExtractor {

    public <I extends Item> SqlParameterSource map(I item) {
        return new MapSqlParameterSource()
                .addValue(COL_ID, getItemId(item))
                .addValue(COL_ITEM_TYPE, item.getType())
                .addValue(COL_NAME, item.getName())
                .addValue(COL_OWNER_ID, item.getOwner().getUserId())
                .addValue(COL_GROUP_ID, item.getGroup().getGroupId())
                .addValue(COL_CREATED_AT, item.getCreated())
                .addValue(COL_MODIFIED_AT, item.getLastModified())
                .addValue(COL_ACCESSED_AT, item.getLastAccessed())
                .addValue(COL_PARENT_ID, getItemId(item.getParent()))
                .addValue(COL_CONTENT_TYPE, getContentType(item));
    }

    private <I extends Item> String getContentType(I item) {
        if (item instanceof File file) {
            return file.getContentType();
        }
        return null;
    }

    private <I extends Item> UUID getItemId(I parent) {
        return ofNullable(parent)
                .map(Item::getHandle)
                .orElse(null);
    }

    public <I extends Item> SqlParameterSource[] mapAccess(I item) {
        List<SqlParameterSource> sources = new ArrayList<>();
        UUID itemId = getItemId(item);
        mapAccess(sources, itemId, item.getOwnerAccess(), ATTR_OWNER);
        mapAccess(sources, itemId, item.getGroupAccess(), ATTR_GROUP);
        mapAccess(sources, itemId, item.getOtherAccess(), ATTR_OTHER);
        return sources.toArray(SqlParameterSource[]::new);
    }

    private void mapAccess(List<SqlParameterSource> sources, UUID itemId, AccessRight access, String attribution) {
        mapRight(sources, itemId, TYPE_READ, attribution, access.isRead());
        mapRight(sources, itemId, TYPE_WRITE, attribution, access.isWrite());
        mapRight(sources, itemId, TYPE_EXECUTE, attribution, access.isExecute());
    }

    private void mapRight(List<SqlParameterSource> sources, UUID itemId, String type, String attribution, boolean value) {
        sources.add(new MapSqlParameterSource()
                .addValue(COL_ITEM_ID, itemId)
                .addValue(COL_TYPE, type)
                .addValue(COL_ATTRIBUTION, attribution)
                .addValue(COL_VALUE, value));
    }
}
