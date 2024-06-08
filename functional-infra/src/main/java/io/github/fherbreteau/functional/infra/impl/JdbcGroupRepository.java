package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.infra.mapper.BooleanResultExtractor;
import io.github.fherbreteau.functional.infra.mapper.GroupExtractor;
import io.github.fherbreteau.functional.infra.mapper.GroupMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_ID;
import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_NAME;
import static io.github.fherbreteau.functional.infra.mapper.UserGroupSQLConstant.COL_GROUP_ID;

@Transactional
public class JdbcGroupRepository implements GroupRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final GroupMapper groupMapper = new GroupMapper();
    private final BooleanResultExtractor existsExtractor = new BooleanResultExtractor();
    private final GroupExtractor groupExtractor = new GroupExtractor();

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
        return findByNameOptional(name).orElseThrow();
    }

    private Optional<Group> findByNameOptional(String name) {
        String query = """
                SELECT id, name
                FROM "group"
                WHERE name = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst();
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
        return findByIdOptional(groupId).orElseThrow();
    }

    private Optional<Group> findByIdOptional(UUID groupId) {
        String query = """
                SELECT id, name
                FROM "group"
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, groupId);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst();
    }

    @Override
    public Group create(Group group) {
        String query = """
                INSERT INTO "group"(id, name) VALUES (:id, :name)
                """;
        jdbcTemplate.update(query, groupExtractor.map(group));
        return group;
    }

    @Override
    public Group update(Group group) {
        Group oldGroup = findByIdOptional(group.getGroupId())
                .or(() -> findByNameOptional(group.getName()))
                .orElseThrow();
        if (!Objects.equals(group.getName(), oldGroup.getName())) {
            String query = """
                    UPDATE "group" SET name = :name WHERE id = :id
                    """;
            jdbcTemplate.update(query, groupExtractor.map(group));
        } else if (!Objects.equals(group.getGroupId(), oldGroup.getGroupId())) {
            String query = """
                    DELETE FROM user_group WHERE group_id = :group_id
                    """;
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue(COL_GROUP_ID, oldGroup.getGroupId());
            jdbcTemplate.update(query, params);
            query = """
                    UPDATE "group" SET id = :id WHERE name = :name
                    """;
            jdbcTemplate.update(query, groupExtractor.map(group));
        }
        return group;
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
