package io.github.fherbreteau.functional.infra.mapper;

import io.github.fherbreteau.functional.domain.entities.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.*;

public class ItemMapper implements RowMapper<Item> {

    private final Map<String, Supplier<AbstractItem.AbstractBuilder<?, ?>>> builderMap = Map.of(
            Folder.class.getSimpleName(), Folder::builder,
            File.class.getSimpleName(), File::builder);

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        AbstractItem.AbstractBuilder<?, ?> builder = builderMap.get(rs.getString(COL_TYPE)).get();
        return builder
                .withName(rs.getString(COL_NAME))
                .withOwner(User.builder(rs.getString(UserSQLConstant.COL_NAME))
                        .withUserId(rs.getObject(UserSQLConstant.COL_ID, UUID.class)).build())
                .withGroup(Group.builder(rs.getString(GroupSQLConstant.COL_NAME))
                        .withGroupId(rs.getObject(GroupSQLConstant.COL_ID, UUID.class)).build())
                .withCreated(rs.getObject(COL_CREATED_AT, LocalDateTime.class))
                .withLastModified(rs.getObject(COL_MODIFIED_AT, LocalDateTime.class))
                .withLastAccessed(rs.getObject(COL_ACCESSED_AT, LocalDateTime.class))
                .build();
    }
}
