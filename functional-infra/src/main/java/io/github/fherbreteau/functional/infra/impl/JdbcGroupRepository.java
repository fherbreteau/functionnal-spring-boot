package io.github.fherbreteau.functional.infra.impl;

import static io.github.fherbreteau.functional.infra.utils.GroupSQLConstants.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.infra.UserGroupRepository;
import io.github.fherbreteau.functional.infra.mapper.ExistsExtractor;
import io.github.fherbreteau.functional.infra.mapper.GroupExtractor;
import io.github.fherbreteau.functional.infra.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupRepository implements GroupRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String groupTable;
    private final UserGroupRepository userGroupRepository;
    private final GroupMapper groupMapper = new GroupMapper();
    private final ExistsExtractor existsExtractor = new ExistsExtractor();
    private final GroupExtractor groupExtractor = new GroupExtractor();

    public JdbcGroupRepository(NamedParameterJdbcTemplate jdbcTemplate,
                               @Value("${database.table.group:group}") String groupTable,
                               UserGroupRepository userGroupRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupTable = groupTable;
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public boolean exists(String name) {
        String query = """
                SELECT 1 FROM %s
                WHERE NAME = :name
                """.formatted(groupTable);
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
                SELECT ID, NAME FROM %s
                WHERE NAME = :name
                """.formatted(groupTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst();
    }

    @Override
    public boolean exists(UUID groupId) {
        String query = """
                SELECT 1 FROM %s
                WHERE ID = :id
                """.formatted(groupTable);
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
                SELECT ID, NAME FROM %s
                WHERE ID = :id
                """.formatted(groupTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, groupId);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst();
    }

    @Override
    public Group create(Group group) {
        String query = """
                INSERT INTO %s (ID, NAME)
                VALUES (:id, :name)
                """.formatted(groupTable);
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
                    UPDATE %s SET NAME = :name
                    WHERE ID = :id
                    """.formatted(groupTable);
            jdbcTemplate.update(query, groupExtractor.map(group));
        } else if (!Objects.equals(group.getGroupId(), oldGroup.getGroupId())) {
            List<UUID> userIds = userGroupRepository.deleteByGroup(oldGroup);
            String query = """
                    UPDATE %s SET ID = :id
                    WHERE NAME = :name
                    """.formatted(groupTable);
            jdbcTemplate.update(query, groupExtractor.map(group));
            userGroupRepository.create(userIds, group);
        }
        return group;
    }

    @Override
    public void delete(Group group) {
        String query = """
                DELETE FROM %s
                WHERE ID = :id AND NAME = :name
                """.formatted(groupTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, group.getGroupId())
                .addValue(COL_NAME, group.getName());
        jdbcTemplate.update(query, params);
    }
}
