package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.ATTR_GROUP;
import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.ATTR_OTHER;
import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.ATTR_OWNER;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.*;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_NAME;
import static io.github.fherbreteau.functional.infra.utils.UserSQLConstants.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import io.github.fherbreteau.functional.domain.entities.AbstractItem;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.FileItem.Builder;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.infra.AccessRightRepository;
import org.springframework.jdbc.core.RowMapper;

public class ItemMapper implements RowMapper<Item> {

    private final Map<String, Supplier<AbstractItem.AbstractBuilder<?, ?>>> builderMap = Map.of(
            Folder.TYPE, Folder::builder,
            File.TYPE, File::builder);

    private final AccessRightRepository accessRightRepository;

    private final Folder parent;

    public ItemMapper(AccessRightRepository accessRightRepository, Folder parent) {
        this.accessRightRepository = accessRightRepository;
        this.parent = parent;
    }

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID itemId = rs.getObject(COL_ID, UUID.class);
        AbstractItem.AbstractBuilder<?, ?> builder = builderMap.get(rs.getString(COL_ITEM_TYPE)).get();
        builder.withName(rs.getString(COL_NAME))
                .withHandle(itemId)
                .withOwner(User.builder(rs.getString(COL_UNAME))
                        .withUserId(rs.getObject(COL_UID, UUID.class)).build())
                .withGroup(Group.builder(rs.getString(COL_GNAME))
                        .withGroupId(rs.getObject(COL_GID, UUID.class)).build())
                .withOwnerAccess(accessRightRepository.getAccess(itemId, ATTR_OWNER))
                .withGroupAccess(accessRightRepository.getAccess(itemId, ATTR_GROUP))
                .withOtherAccess(accessRightRepository.getAccess(itemId, ATTR_OTHER))
                .withCreated(rs.getObject(COL_CREATED_AT, LocalDateTime.class))
                .withLastModified(rs.getObject(COL_MODIFIED_AT, LocalDateTime.class))
                .withLastAccessed(rs.getObject(COL_ACCESSED_AT, LocalDateTime.class))
                .withParent(parent);
        if (builder instanceof Builder fileBuilder) {
            fileBuilder.withContentType(rs.getString(COL_CONTENT_TYPE));
        }
        return builder.build();
    }
}
