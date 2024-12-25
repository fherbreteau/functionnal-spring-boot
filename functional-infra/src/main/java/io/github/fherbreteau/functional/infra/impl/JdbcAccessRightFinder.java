package io.github.fherbreteau.functional.infra.impl;

import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.COL_ATTRIBUTION;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.COL_ITEM_ID;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.TYPE_EXECUTE;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.TYPE_READ;
import static io.github.fherbreteau.functional.infra.mapper.ItemAccessSQLConstants.TYPE_WRITE;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.infra.AccessRightFinder;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAccessRightFinder implements AccessRightFinder {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcAccessRightFinder(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AccessRight getAccess(UUID itemId, String attribution) {
        String query = "SELECT TYPE FROM ITEM_ACCESS WHERE ITEM_ID = :item_id AND ATTRIBUTION = :attribution AND" +
                " \"VALUE\" = TRUE";
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
}
