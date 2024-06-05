package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.driven.GroupRepository;
import io.github.fherbreteau.functional.infra.mapper.BooleanResultExtractor;
import io.github.fherbreteau.functional.infra.mapper.GroupMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_ID;
import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_NAME;

@Transactional
public class JdbcGroupRepository implements GroupRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final GroupMapper groupMapper = new GroupMapper();
    private final BooleanResultExtractor existsExtractor = new BooleanResultExtractor();

    public JdbcGroupRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean exists(String name) {
        String query = """
                SELECT 1
                FROM "group"
                WHERE name = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public Group findByName(String name) {
        String query = """
                SELECT id, name
                FROM "group"
                WHERE name = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst().orElseThrow();
    }

    @Override
    public boolean exists(UUID groupId) {
        String query = """
                SELECT 1
                FROM "group"
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, groupId);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public Group findById(UUID groupId) {
        String query = """
                SELECT id, name
                FROM "group"
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, groupId);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst().orElseThrow();
    }

    @Override
    public Group save(Group group) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void delete(Group group) {
        String query = """
                DELETE FROM "group"
                WHERE id = :id
                AND name = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, group.getGroupId())
                .addValue(COL_NAME, group.getName());
        jdbcTemplate.update(query, params);
    }
}
