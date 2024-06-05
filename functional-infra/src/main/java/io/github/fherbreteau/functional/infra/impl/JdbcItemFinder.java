package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.infra.ItemFinder;
import io.github.fherbreteau.functional.infra.mapper.ItemMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.*;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.*;
import static java.util.Objects.isNull;

@Transactional
public class JdbcItemFinder implements ItemFinder, InitializingBean {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ItemMapper itemMapper = new ItemMapper();

    public JdbcItemFinder(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UUID getItemId(Item item) {
        String query = """
                SELECT i1.id
                FROM item i1
                JOIN item i2 ON i1.parent_id = i2.id
                WHERE i2.name = :parent_name
                AND i1.name = :item_name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PARENT_NAME, item.getParent().getName())
                .addValue(COL_NAME, item.getName());
        return jdbcTemplate.queryForStream(query, params, new SingleColumnRowMapper<UUID>())
                .findFirst().orElse(null);
    }

    @Override
    public Folder getFolder(UUID folderId) {
        if (isNull(folderId)) {
            return null;
        }
        String query = """
                SELECT type, name, u.id as uid, u.name as uname, g.id as gid, g.name as gname,
                       create_at, modified_at, accessed_at, content_type, parent_id
                FROM ITEM
                JOIN user u ON owner = u.id
                JOIN group g ON group = g.id
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, folderId);
        return (Folder) jdbcTemplate.queryForObject(query, params, itemMapper);
    }

    @Override
    public AccessRight getAccess(UUID itemId, String attribution) {
        String query = """
                SELECT type
                FROM item_access
                WHERE item_id = :item_id
                AND attribution = :attribution
                AND value = 1;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ITEM_ID, itemId)
                .addValue(COL_ATTRIBUTION, attribution);
        List<String> accessTypes = jdbcTemplate.query(query, params, new SingleColumnRowMapper<>());
        AccessRight right = AccessRight.none();
        if (accessTypes.contains(TYPE_READ)) {
            right = right.addRead();
        }
        if (accessTypes.contains(TYPE_WRITE)) {
            right = right.addWrite();
        }
        if (accessTypes.contains(TYPE_EXECUTE)) {
            right = right.addExecute();
        }
        return right;
    }

    @Override
    public void afterPropertiesSet() {
        itemMapper.setItemFinder(this);
    }
}
