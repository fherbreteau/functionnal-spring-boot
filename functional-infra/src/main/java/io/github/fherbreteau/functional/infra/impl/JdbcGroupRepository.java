package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.driven.repository.GroupRepository;
import io.github.fherbreteau.functional.infra.mapper.BooleanResultExtractor;
import io.github.fherbreteau.functional.infra.mapper.GroupExtractor;
import io.github.fherbreteau.functional.infra.mapper.GroupMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_ID;
import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_NAME;
import static io.github.fherbreteau.functional.infra.mapper.UserGroupSQLConstant.COL_GROUP_ID;

@Repository
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
        String query = "SELECT 1 FROM \"GROUP\" WHERE NAME = :name";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public Group findByName(String name) {
        return findByNameOptional(name).orElseThrow();
    }

    private Optional<Group> findByNameOptional(String name) {
        String query = "SELECT ID, NAME FROM \"GROUP\" WHERE NAME = :name";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst();
    }

    @Override
    public boolean exists(UUID groupId) {
        String query = "SELECT 1 FROM \"GROUP\" WHERE ID = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, groupId);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public Group findById(UUID groupId) {
        return findByIdOptional(groupId).orElseThrow();
    }

    private Optional<Group> findByIdOptional(UUID groupId) {
        String query = "SELECT ID, NAME FROM \"GROUP\" WHERE ID = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, groupId);
        return jdbcTemplate.queryForStream(query, params, groupMapper)
                .findFirst();
    }

    @Override
    public Group create(Group group) {
        String query = "INSERT INTO \"GROUP\"(ID, NAME) VALUES (:id, :name)";
        jdbcTemplate.update(query, groupExtractor.map(group));
        return group;
    }

    @Override
    public Group update(Group group) {
        Group oldGroup = findByIdOptional(group.getGroupId())
                .or(() -> findByNameOptional(group.getName()))
                .orElseThrow();
        if (!Objects.equals(group.getName(), oldGroup.getName())) {
            String query = "UPDATE \"GROUP\" SET NAME = :name WHERE ID = :id";
            jdbcTemplate.update(query, groupExtractor.map(group));
        } else if (!Objects.equals(group.getGroupId(), oldGroup.getGroupId())) {
            String query = "DELETE FROM USER_GROUP WHERE GROUP_id = :group_id";
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue(COL_GROUP_ID, oldGroup.getGroupId());
            jdbcTemplate.update(query, params);
            query = "UPDATE \"GROUP\" SET ID = :id WHERE NAME = :name";
            jdbcTemplate.update(query, groupExtractor.map(group));
        }
        return group;
    }

    @Override
    public void delete(Group group) {
        String query = "DELETE FROM \"GROUP\" WHERE ID = :id AND NAME = :name";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, group.getGroupId())
                .addValue(COL_NAME, group.getName());
        jdbcTemplate.update(query, params);
    }
}
