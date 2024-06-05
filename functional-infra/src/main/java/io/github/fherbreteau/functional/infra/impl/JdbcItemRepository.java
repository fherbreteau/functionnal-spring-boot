package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.ItemRepository;
import io.github.fherbreteau.functional.infra.ItemFinder;
import io.github.fherbreteau.functional.infra.mapper.BooleanResultExtractor;
import io.github.fherbreteau.functional.infra.mapper.ItemExtractor;
import io.github.fherbreteau.functional.infra.mapper.ItemMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.*;
import static java.util.Objects.isNull;

@Transactional
public class JdbcItemRepository implements ItemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ItemFinder itemFinder;
    private final ItemMapper itemMapper = new ItemMapper();
    private final ItemExtractor itemExtractor = new ItemExtractor();
    private final BooleanResultExtractor existsExtractor = new BooleanResultExtractor();

    public JdbcItemRepository(NamedParameterJdbcTemplate jdbcTemplate,
                              ItemFinder itemFinder) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemFinder = itemFinder;
        itemMapper.setItemFinder(itemFinder);
        itemExtractor.setItemFinder(itemFinder);
    }

    @Override
    public boolean exists(Folder parent, String name) {
        String query = """
                SELECT 1
                FROM item i1
                JOIN item i2 ON i1.parent_id = i2.id
                WHERE i2."name" = :parent_name
                AND i1."name" = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_PARENT_NAME, parent.getName())
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public <I extends Item> I save(I item) {
        UUID itemId = itemFinder.getItemId(item);
        if (isNull(itemId)) {
            return insert(item);
        }
        return update(itemId, item);
    }

    private <I extends Item> I insert(I item) {
        String query = """
                INSERT INTO item(id, name, type, owner, group, create_at, modified_at, accessed_at, parent_id, content_type)
                VALUES (:id, :name, :type, :owner, :group, :create_at, :modified_at, :accessed_at, :parent_id, :content_type)
                """;
        UUID itemId = UUID.randomUUID();
        jdbcTemplate.update(query, itemExtractor.map(itemId, item));
        query = """
                INSERT INTO item_access(item_id, type, attribution, value)
                VALUES(:item_id, :type, :attribution, :value)
                """;
        jdbcTemplate.batchUpdate(query, itemExtractor.mapAccess(itemId, item));
        return item;
    }

    private <I extends Item> I update(UUID itemId, I item) {
        String query = """
                UPDATE item
                SET name = :name,
                    type = :type,
                    owner = :owner,
                    group = :group,
                    create_at = :created_at,
                    modified_at = :modified_at,
                    accessed_at = :accessed_at,
                    parent_id = :parent_id,
                    content_type = :content_type
                WHERE id = :id
                """;
        jdbcTemplate.update(query, itemExtractor.map(itemId, item));
        query = """
                UPDATE item_access
                SET value = :value
                WHERE item_id = :item_id
                AND type = :type
                AND attribution = :attribution
                """;
        jdbcTemplate.batchUpdate(query, itemExtractor.mapAccess(itemId, item));
        return item;
    }

    @Override
    public <I extends Item> void delete(I item) {
        UUID itemId = itemFinder.getItemId(item);
        if (isNull(itemId)) {
            return;
        }
        String query = """
                DELETE FROM item
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, itemId);
        jdbcTemplate.update(query, params);
    }

    @Override
    public List<Item> findByParentAndUser(Folder folder, User actor) {
        UUID folderId = itemFinder.getItemId(folder);
        if (isNull(folderId)) {
            return List.of();
        }
        String query = """
                SELECT type, name, u.id as uid, u.name as uname, g.id as gid, g.name as gname,
                       create_at, modified_at, accessed_at, content_type, parent_id
                FROM item i
                JOIN account u ON i.owner = u.id
                JOIN group g ON i.group = g.id
                JOIN item_access a ON i.id = a.item_id
                WHERE parent_id = :parent_id
                AND (u.id = :owner OR g.id IN (:groups) OR
                    (a.type = 'READ' AND a.attribution = 'OTHER' AND a.value = 1))
                """;
        List<UUID> groupIds = actor.getGroups().stream().map(Group::getGroupId).toList();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, folderId)
                .addValue(COL_OWNER_ID, actor.getUserId())
                .addValue(PARAM_GROUP_IDS, groupIds);
        return jdbcTemplate.query(query, params, itemMapper);
    }

    @Override
    public Optional<Item> findByNameAndParentAndUser(String name, Folder folder, User actor) {
        UUID folderId = itemFinder.getItemId(folder);
        if (isNull(folderId)) {
            return Optional.empty();
        }
        String query = """
                SELECT type, name, u.id as uid, u.name as uname, g.id as gid, g.name as gname,
                       create_at, modified_at, accessed_at, content_type, parent_id
                FROM item i
                JOIN account u ON i.owner = u.id
                JOIN group g ON i.group = g.id
                JOIN item_access a ON i.id = a.item_id
                WHERE parent_id = :parent_id
                AND (u.id = :owner OR g.id = :group OR
                    (a.type = 'READ' AND a.attribution = 'OTHER' AND a.value = 1))
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, folderId);
        return jdbcTemplate.queryForStream(query, params, itemMapper).findFirst();
    }
}
