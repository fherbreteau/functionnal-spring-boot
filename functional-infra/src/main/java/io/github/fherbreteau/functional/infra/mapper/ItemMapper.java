package io.github.fherbreteau.functional.infra.mapper;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.infra.AccessRightFinder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.*;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.*;

public class ItemMapper implements RowMapper<Item> {

    private final Map<String, Supplier<AbstractItem.AbstractBuilder<?, ?>>> builderMap = Map.of(
            Folder.class.getSimpleName(), Folder::builder,
            File.class.getSimpleName(), File::builder);

    private final AccessRightFinder accessRightFinder;

    private final Folder parent;

    public ItemMapper(AccessRightFinder accessRightFinder, Folder parent) {
        this.accessRightFinder = accessRightFinder;
        this.parent = parent;
    }

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID itemId = rs.getObject(COL_ID, UUID.class);
        AbstractItem.AbstractBuilder<?, ?> builder = builderMap.get(rs.getString(COL_ITEM_TYPE)).get();
        builder.withName(rs.getString(COL_NAME))
                .withHandle(itemId)
                .withOwner(User.builder(rs.getString(UserSQLConstant.COL_USER_NAME))
                        .withUserId(rs.getObject(UserSQLConstant.COL_USER_ID, UUID.class)).build())
                .withGroup(Group.builder(rs.getString(GroupSQLConstant.COL_GROUP_NAME))
                        .withGroupId(rs.getObject(GroupSQLConstant.COL_GROUP_ID, UUID.class)).build())
                .withOwnerAccess(accessRightFinder.getAccess(itemId, ATTR_OWNER))
                .withGroupAccess(accessRightFinder.getAccess(itemId, ATTR_GROUP))
                .withOtherAccess(accessRightFinder.getAccess(itemId, ATTR_OTHER))
                .withCreated(rs.getObject(COL_CREATED_AT, LocalDateTime.class))
                .withLastModified(rs.getObject(COL_MODIFIED_AT, LocalDateTime.class))
                .withLastAccessed(rs.getObject(COL_ACCESSED_AT, LocalDateTime.class))
                .withParent(parent);
        if (builder instanceof File.Builder fileBuilder) {
            fileBuilder.withContentType(rs.getString(COL_CONTENT_TYPE));
        }
        return builder.build();
    }
}
