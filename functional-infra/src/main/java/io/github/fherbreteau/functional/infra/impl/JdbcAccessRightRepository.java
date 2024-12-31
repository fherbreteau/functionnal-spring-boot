package io.github.fherbreteau.functional.infra.impl;

import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.COL_ATTRIBUTION;
import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.COL_ITEM_ID;
import static io.github.fherbreteau.functional.infra.utils.ItemSQLConstants.COL_ID;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.infra.AccessRightRepository;
import io.github.fherbreteau.functional.infra.mapper.ItemAccessExtractor;
import io.github.fherbreteau.functional.infra.mapper.ItemAccessMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAccessRightRepository implements AccessRightRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String itemAccessTable;
    private final ItemAccessMapper itemAccessMapper = new ItemAccessMapper();
    private final ItemAccessExtractor itemAccessExtractor = new ItemAccessExtractor();

    public JdbcAccessRightRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                     @Value("${database.table.item-access:item_access}") String itemAccessTable) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemAccessTable = itemAccessTable;
    }

    @Override
    public void createAccess(UUID itemId, AccessRight accessRight, String attribution) {
        String query = "INSERT INTO item_access(ITEM_ID, TYPE, ATTRIBUTION, \"VALUE\") " +
                "VALUES(:item_id, :type, :attribution, :value)";
        jdbcTemplate.batchUpdate(query, itemAccessExtractor.map(itemId, accessRight, attribution));
    }

    @Override
    public AccessRight getAccess(UUID itemId, String attribution) {
        String query = """
                SELECT TYPE FROM %s
                WHERE ITEM_ID = :item_id AND ATTRIBUTION = :attribution AND "VALUE" = TRUE
                """.formatted(itemAccessTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ITEM_ID, itemId)
                .addValue(COL_ATTRIBUTION, attribution);
        List<String> accessTypes = jdbcTemplate.query(query, params, new SingleColumnRowMapper<>());
        return itemAccessMapper.map(accessTypes);
    }

    @Override
    public void updateAccess(UUID itemId, AccessRight accessRight, String attribution) {
        String query = """
                UPDATE %s
                SET "VALUE" = :value
                WHERE ITEM_ID = :item_id AND TYPE = :type AND ATTRIBUTION = :attribution
                """.formatted(itemAccessTable);
        jdbcTemplate.batchUpdate(query, itemAccessExtractor.map(itemId, accessRight, attribution));
    }

    @Override
    public void deleteAccess(UUID itemId, String attribution) {
        String query = """
                DELETE FROM %s
                WHERE ITEM_ID = :id AND ATTRIBUTION = :attribution
                """.formatted(itemAccessTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, itemId)
                .addValue(COL_ATTRIBUTION, attribution);
        jdbcTemplate.update(query, params);

    }
}
