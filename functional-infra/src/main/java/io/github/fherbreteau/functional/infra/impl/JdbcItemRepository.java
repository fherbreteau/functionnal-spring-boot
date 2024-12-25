package io.github.fherbreteau.functional.infra.impl;

import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_NAME;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_OWNER_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_PARENT_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.PARAM_FORCE;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.PARAM_GROUP_IDS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AbstractItem;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
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
import org.springframework.stereotype.Repository;

@Repository
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
        String query = "SELECT 1 FROM ITEM WHERE NAME = :name AND PARENT_ID = :parent_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, parent.getHandle())
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public boolean exists(Item item) {
        UUID handle = getItemHandle(item);
        if (Objects.nonNull(handle)) {
            String query = "SELECT 1 FROM ITEM WHERE ID = :id";
            SqlParameterSource params = new MapSqlParameterSource(COL_ID, handle);
            return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
        }
        return exists(item.getParent(), item.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends Item> I create(I item) {
        String query = "INSERT INTO ITEM(NAME, TYPE, OWNER, \"GROUP\", CREATED_AT, MODIFIED_AT, ACCESSED_AT, " +
                "PARENT_ID, CONTENT_TYPE) VALUES (:name, :type, :owner, :group, :created_at, :modified_at, " +
                ":accessed_at, :parent_id, :content_type)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, itemExtractor.map(item), keyHolder, new String[]{COL_ID});
        I inserted = (I) item.copyBuilder().withHandle(keyHolder.getKeyAs(UUID.class)).build();
        query = "INSERT INTO item_access(ITEM_ID, TYPE, ATTRIBUTION, \"VALUE\") " +
                "VALUES(:item_id, :type, :attribution, :value)";
        jdbcTemplate.batchUpdate(query, itemExtractor.mapAccess(inserted));
        return inserted;
    }

    @Override
    public <I extends Item> I update(I item) {
        String query = "UPDATE ITEM SET NAME = :name, TYPE = :type, OWNER = :owner, \"GROUP\" = :group, CREATED_AT = " +
                ":created_at, MODIFIED_AT = :modified_at, ACCESSED_AT = :accessed_at, PARENT_ID = :parent_id, " +
                "CONTENT_TYPE = :content_type WHERE id = :id";
        jdbcTemplate.update(query, itemExtractor.map(item));
        query = "UPDATE item_access SET \"VALUE\" = :value WHERE ITEM_ID = :item_id AND TYPE = :type AND ATTRIBUTION = " +
                ":attribution";
        jdbcTemplate.batchUpdate(query, itemExtractor.mapAccess(item));
        return item;
    }

    @Override
    public <I extends Item> void delete(I item) {
        // Delete all item_access of the item
        String query = "DELETE FROM ITEM_ACCESS WHERE ITEM_ID = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, getItemHandle(item));
        jdbcTemplate.update(query, params);
        // Delete the item
        query = "DELETE FROM ITEM WHERE id = :id";
        params = new MapSqlParameterSource()
                .addValue(COL_ID, getItemHandle(item));
        jdbcTemplate.update(query, params);
    }

    @Override
    public List<Item> findByParentAndUser(Folder folder, User actor) {
        String query = "SELECT DISTINCT i.ID as id, i.TYPE as type, i.NAME as name, u.ID as uid, u.NAME as uname, " +
                "g.ID as gid, g.NAME as gname, CREATED_AT, MODIFIED_AT, ACCESSED_AT, CONTENT_TYPE, PARENT_ID FROM" +
                " ITEM i JOIN \"USER\" u ON i.OWNER = u.ID JOIN \"GROUP\" g ON i.\"GROUP\" = g.ID LEFT JOIN " +
                "ITEM_ACCESS a ON i.ID = a.ITEM_ID WHERE PARENT_ID = :parent_id AND (u.ID = :owner OR g.ID in " +
                "(:group_ids) OR (a.TYPE = 'READ' AND a.ATTRIBUTION = 'OTHER' AND a.\"VALUE\" = TRUE) OR :force)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_PARENT_ID, getItemHandle(folder))
                .addValue(COL_OWNER_ID, actor.getUserId())
                .addValue(PARAM_GROUP_IDS, getGroupIds(actor))
                .addValue(PARAM_FORCE, Objects.equals(actor, User.root()));
        return jdbcTemplate.query(query, params, new ItemMapper(accessRightFinder, folder));
    }

    @Override
    public Optional<Item> findByNameAndParentAndUser(String name, Folder folder, User actor) {
        String query = " SELECT i.ID as id, i.TYPE as type, i.NAME as name, u.ID as uid, u.NAME as uname, g.ID as " +
                "gid, g.NAME as gname, CREATED_AT, MODIFIED_AT, ACCESSED_AT, CONTENT_TYPE, PARENT_ID FROM ITEM i JOIN" +
                " \"USER\" u ON i.OWNER = u.ID JOIN \"GROUP\" g ON i.\"GROUP\" = g.ID LEFT JOIN ITEM_ACCESS a " +
                "ON i.ID = a.ITEM_ID WHERE i.NAME = :name AND PARENT_ID = :parent_id AND (u.ID = :owner OR g.ID in " +
                "(:group_ids) OR (a.TYPE = 'READ' AND a.ATTRIBUTION = 'OTHER' AND a.\"VALUE\" = TRUE) OR :force)";
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

    private <I extends Item> UUID getItemHandle(I item) {
        return ((AbstractItem<?, ?>) item).getHandle();
    }
}
