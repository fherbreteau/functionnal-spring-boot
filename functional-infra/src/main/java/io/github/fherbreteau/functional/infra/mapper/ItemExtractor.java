package io.github.fherbreteau.functional.infra.mapper;

import io.github.fherbreteau.functional.domain.entities.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.*;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.*;
import static java.util.Optional.ofNullable;

public class ItemExtractor {

    public <I extends Item> SqlParameterSource map(I item) {
        return new MapSqlParameterSource()
                .addValue(COL_ID, getItemId(item))
                .addValue(COL_ITEM_TYPE, item.getClass().getSimpleName())
                .addValue(COL_NAME, item.getName())
                .addValue(COL_OWNER_ID, item.getOwner().getUserId())
                .addValue(COL_GROUP_ID, item.getGroup().getGroupId())
                .addValue(COL_CREATED_AT, item.getCreated())
                .addValue(COL_MODIFIED_AT, item.getLastModified())
                .addValue(COL_ACCESSED_AT, item.getLastModified())
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
                .map(AbstractItem.class::cast)
                .map(AbstractItem::getHandle).orElse(null);
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
