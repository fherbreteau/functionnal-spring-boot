package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.*;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.infra.AccessRightFinder;
import io.github.fherbreteau.functional.infra.mapper.BooleanResultExtractor;
import io.github.fherbreteau.functional.infra.mapper.ItemExtractor;
import io.github.fherbreteau.functional.infra.mapper.ItemMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.*;

@Transactional
public class JdbcItemRepository implements ItemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AccessRightFinder accessRightFinder;
    private final ItemExtractor itemExtractor = new ItemExtractor();
    private final BooleanResultExtractor existsExtractor = new BooleanResultExtractor();

    public JdbcItemRepository(NamedParameterJdbcTemplate jdbcTemplate,
                              AccessRightFinder accessRightFinder) {
        this.jdbcTemplate = jdbcTemplate;
        this.accessRightFinder = accessRightFinder;
    }

    @Override
    public boolean exists(Folder parent, String name) {
        String query = """
                SELECT 1
                FROM item
                WHERE name = :name
                AND parent_id = :parent_id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, parent.getHandle())
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends Item> I create(I item) {
        String query = """
                INSERT INTO item(name, type, owner, "group", created_at, modified_at, accessed_at, parent_id, content_type)
                VALUES (:name, :type, :owner, :group, :created_at, :modified_at, :accessed_at, :parent_id, :content_type)
                 RETURNING id
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, itemExtractor.map(item), keyHolder);
        I inserted = (I) item.copyBuilder().withHandle(keyHolder.getKeyAs(UUID.class)).build();
        query = """
                INSERT INTO item_access(item_id, type, attribution, value)
                VALUES(:item_id, :type, :attribution, :value)
                """;
        jdbcTemplate.batchUpdate(query, itemExtractor.mapAccess(inserted));
        return inserted;
    }

    @Override
    public  <I extends Item> I update(I item) {
        String query = """
                UPDATE item
                SET name = :name,
                    type = :type,
                    owner = :owner,
                    "group" = :group,
                    created_at = :created_at,
                    modified_at = :modified_at,
                    accessed_at = :accessed_at,
                    parent_id = :parent_id,
                    content_type = :content_type
                WHERE id = :id
                """;
        jdbcTemplate.update(query, itemExtractor.map(item));
        query = """
                UPDATE item_access
                SET value = :value
                WHERE item_id = :item_id
                AND type = :type
                AND attribution = :attribution
                """;
        jdbcTemplate.batchUpdate(query, itemExtractor.mapAccess(item));
        return item;
    }

    @Override
    public <I extends Item> void delete(I item) {
        String query = """
                DELETE FROM item
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, getItemHandle(item));
        jdbcTemplate.update(query, params);
    }

    @Override
    public List<Item> findByParentAndUser(Folder folder, User actor) {
        String query = """
                SELECT DISTINCT i.id as id, i.type as type, i.name as name, u.id as uid, u.name as uname, g.id as gid,
                       g.name as gname, created_at, modified_at, accessed_at, content_type, parent_id
                FROM item i
                JOIN "user" u ON i.owner = u.id
                JOIN "group" g ON i.group = g.id
                LEFT JOIN item_access a ON i.id = a.item_id
                WHERE parent_id = :parent_id
                AND (u.id = :owner OR g.id in (:group_ids) OR
                    (a.type = 'READ' AND a.attribution = 'OTHER' AND a.value = TRUE) OR
                    :force)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, getItemHandle(folder))
                .addValue(COL_OWNER_ID, actor.getUserId())
                .addValue(PARAM_GROUP_IDS, getGroupIds(actor))
                .addValue(PARAM_FORCE, Objects.equals(actor, User.root()));
        return jdbcTemplate.query(query, params, new ItemMapper(accessRightFinder, folder));
    }

    @Override
    public Optional<Item> findByNameAndParentAndUser(String name, Folder folder, User actor) {
        String query = """
                SELECT i.id as id, i.type as type, i.name as name, u.id as uid, u.name as uname, g.id as gid,
                       g.name as gname, created_at, modified_at, accessed_at, content_type, parent_id
                FROM item i
                JOIN "user" u ON i.owner = u.id
                JOIN "group" g ON i.group = g.id
                LEFT JOIN item_access a ON i.id = a.item_id
                WHERE i.name = :name
                AND parent_id = :parent_id
                AND (u.id = :owner OR g.id in (:group_ids) OR
                    (a.type = 'READ' AND a.attribution = 'OTHER' AND a.value = TRUE) OR
                    :force)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name)
                .addValue(COL_PARENT_ID, getItemHandle(folder))
                .addValue(COL_OWNER_ID, actor.getUserId())
                .addValue(PARAM_GROUP_IDS, getGroupIds(actor))
                .addValue(PARAM_FORCE, Objects.equals(actor, User.root()));
        return jdbcTemplate.queryForStream(query, params, new ItemMapper(accessRightFinder, folder)).findFirst();
    }

    private List<UUID> getGroupIds(User actor) {
        return actor.getGroups().stream().map(Group::getGroupId).toList();
    }

    private <I extends  Item> UUID getItemHandle(I item) {
        return ((AbstractItem<?, ?>) item).getHandle();
    }
}
