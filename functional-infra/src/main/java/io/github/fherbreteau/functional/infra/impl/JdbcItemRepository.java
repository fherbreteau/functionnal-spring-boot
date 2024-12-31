package io.github.fherbreteau.functional.infra.impl;

import static io.github.fherbreteau.functional.infra.utils.ExtractorUtils.getGroupIds;
import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.*;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_NAME;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_OWNER_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_PARENT_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.PARAM_FORCE;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.PARAM_GROUP_IDS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.infra.AccessRightRepository;
import io.github.fherbreteau.functional.infra.mapper.ExistsExtractor;
import io.github.fherbreteau.functional.infra.mapper.ItemExtractor;
import io.github.fherbreteau.functional.infra.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcItemRepository implements ItemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String groupTable;
    private final String itemTable;
    private final String itemAccessTable;
    private final String userTable;
    private final AccessRightRepository accessRightRepository;
    private final ItemExtractor itemExtractor = new ItemExtractor();
    private final ExistsExtractor existsExtractor = new ExistsExtractor();

    public JdbcItemRepository(NamedParameterJdbcTemplate jdbcTemplate,
                              AccessRightRepository accessRightRepository,
                              @Value("${database.table.group:group}") String groupTable,
                              @Value("${database.table.item:item}") String itemTable,
                              @Value("${database.table.item-access:item_access}") String itemAccessTable,
                              @Value("${database.table.user:user}") String userTable) {
        this.jdbcTemplate = jdbcTemplate;
        this.accessRightRepository = accessRightRepository;
        this.groupTable = groupTable;
        this.itemTable = itemTable;
        this.itemAccessTable = itemAccessTable;
        this.userTable = userTable;
    }

    @Override
    public boolean exists(Folder parent, String name) {
        String query = """
                SELECT 1 FROM %s
                WHERE NAME = :name AND PARENT_ID = :parent_id
                """.formatted(itemTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, parent.getHandle())
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public boolean exists(Item item) {
        UUID handle = item.getHandle();
        if (Objects.nonNull(handle)) {
            String query = """
                    SELECT 1 FROM %s
                    WHERE ID = :id
                    """.formatted(itemTable);
            SqlParameterSource params = new MapSqlParameterSource(COL_ID, handle);
            return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
        }
        return exists(item.getParent(), item.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends Item> I create(I item) {
        String query = """
                INSERT INTO %s (NAME, TYPE, OWNER, "GROUP", CREATED_AT, MODIFIED_AT, ACCESSED_AT, PARENT_ID, CONTENT_TYPE)
                VALUES (:name, :type, :owner, :group, :created_at, :modified_at, :accessed_at, :parent_id, :content_type)
                """.formatted(itemTable);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, itemExtractor.map(item), keyHolder, new String[]{COL_ID});
        I inserted = (I) item.copyBuilder().withHandle(keyHolder.getKeyAs(UUID.class)).build();
        accessRightRepository.createAccess(inserted.getHandle(), item.getOwnerAccess(), ATTR_OWNER);
        accessRightRepository.createAccess(inserted.getHandle(), item.getGroupAccess(), ATTR_GROUP);
        accessRightRepository.createAccess(inserted.getHandle(), item.getOtherAccess(), ATTR_OTHER);
        return inserted;
    }

    @Override
    public <I extends Item> I update(I item) {
        String query = """
                UPDATE %s
                SET NAME = :name, TYPE = :type, OWNER = :owner,"GROUP" = :group, CREATED_AT = :created_at,
                MODIFIED_AT = :modified_at, ACCESSED_AT = :accessed_at, PARENT_ID = :parent_id,
                CONTENT_TYPE = :content_type
                WHERE id = :id
                """.formatted(itemTable);
        jdbcTemplate.update(query, itemExtractor.map(item));
        accessRightRepository.updateAccess(item.getHandle(), item.getOwnerAccess(), ATTR_OWNER);
        accessRightRepository.updateAccess(item.getHandle(), item.getGroupAccess(), ATTR_GROUP);
        accessRightRepository.updateAccess(item.getHandle(), item.getOtherAccess(), ATTR_OTHER);
        return item;
    }

    @Override
    public <I extends Item> void delete(I item) {
        // Delete all item_access of the item
        accessRightRepository.deleteAccess(item.getHandle(), ATTR_OWNER);
        accessRightRepository.deleteAccess(item.getHandle(), ATTR_GROUP);
        accessRightRepository.deleteAccess(item.getHandle(), ATTR_OTHER);
        // Delete the item
        String query = """
                DELETE FROM %s
                WHERE id = :id
                """.formatted(itemTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, item.getHandle());
        jdbcTemplate.update(query, params);
    }

    @Override
    public List<Item> findByParentAndUser(Folder folder, User actor) {
        String query = """
                SELECT DISTINCT i.ID as id, i.TYPE as type, i.NAME as name, u.ID as uid, u.NAME as uname, g.ID as gid,
                 g.NAME as gname, CREATED_AT, MODIFIED_AT, ACCESSED_AT, CONTENT_TYPE, PARENT_ID
                FROM %s i
                JOIN %s u ON i.OWNER = u.ID
                JOIN %s g ON i."GROUP" = g.ID
                LEFT JOIN %s a ON i.ID = a.ITEM_ID
                WHERE PARENT_ID = :parent_id
                AND (u.ID = :owner OR g.ID in (:group_ids)
                 OR (a.TYPE = 'READ' AND a.ATTRIBUTION = 'OTHER' AND a."VALUE" = TRUE)
                 OR :force)
                """.formatted(itemTable, userTable, groupTable, itemAccessTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, folder.getHandle())
                .addValue(COL_OWNER_ID, actor.getUserId())
                .addValue(PARAM_GROUP_IDS, getGroupIds(actor))
                .addValue(PARAM_FORCE, Objects.equals(actor, User.root()));
        return jdbcTemplate.query(query, params, new ItemMapper(accessRightRepository, folder));
    }

    @Override
    public Optional<Item> findByNameAndParentAndUser(String name, Folder folder, User actor) {
        String query = """
                SELECT i.ID as id, i.TYPE as type, i.NAME as name, u.ID as uid, u.NAME as uname, g.ID as gid,
                 g.NAME as gname, CREATED_AT, MODIFIED_AT, ACCESSED_AT, CONTENT_TYPE, PARENT_ID
                FROM %s i
                JOIN %s u ON i.OWNER = u.ID
                JOIN %s g ON i."GROUP" = g.ID
                LEFT JOIN %s a ON i.ID = a.ITEM_ID
                WHERE i.NAME = :name AND PARENT_ID = :parent_id
                AND (u.ID = :owner
                  OR g.ID in (:group_ids)
                  OR (a.TYPE = 'READ' AND a.ATTRIBUTION = 'OTHER' AND a."VALUE" = TRUE)
                  OR :force)
                """.formatted(itemTable, userTable, groupTable, itemAccessTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name)
                .addValue(COL_PARENT_ID, folder.getHandle())
                .addValue(COL_OWNER_ID, actor.getUserId())
                .addValue(PARAM_GROUP_IDS, getGroupIds(actor))
                .addValue(PARAM_FORCE, Objects.equals(actor, User.root()));
        return jdbcTemplate.queryForStream(query, params, new ItemMapper(accessRightRepository, folder)).findFirst();
    }

}
