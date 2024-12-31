package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_ACCESSED_AT;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_CONTENT_TYPE;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_CREATED_AT;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_GROUP_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_ITEM_TYPE;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_MODIFIED_AT;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_NAME;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_OWNER_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_PARENT_ID;
import static java.util.Optional.ofNullable;

import java.util.UUID;

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
}
