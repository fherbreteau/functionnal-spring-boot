package io.github.fherbreteau.functional.infra.impl;

import static io.github.fherbreteau.functional.infra.utils.UserGroupSQLConstants.COL_GROUP_ID;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.infra.UserGroupRepository;
import io.github.fherbreteau.functional.infra.mapper.UserGroupExtractor;
import io.github.fherbreteau.functional.infra.utils.UserGroupSQLConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserGroupRepository implements UserGroupRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String userGroupTable;
    private final UserGroupExtractor userGroupExtractor = new UserGroupExtractor();

    public JdbcUserGroupRepository(NamedParameterJdbcTemplate jdbcTemplate,
                                   @Value("${database.table.user-group:user-group}") String userGroupTable) {
        this.jdbcTemplate = jdbcTemplate;
        this.userGroupTable = userGroupTable;
    }

    @Override
    public void create(User user, List<UUID> groupIds) {
        String query = """
                    INSERT INTO %s (USER_ID, GROUP_ID)
                    VALUES (:user_id, :group_id)
                    """.formatted(userGroupTable);
        jdbcTemplate.batchUpdate(query, userGroupExtractor.map(user, groupIds));
    }

    @Override
    public void create(List<UUID> userIds, Group group) {
        String query = """
                    INSERT INTO %s (USER_ID, GROUP_ID)
                    VALUES (:user_id, :group_id)
                    """.formatted(userGroupTable);
        jdbcTemplate.batchUpdate(query, userGroupExtractor.map(userIds, group));
    }

    @Override
    public List<UUID> deleteByUser(User user) {
        String query = """
                    SELECT GROUP_ID FROM %s
                    WHERE USER_ID = :user_id
                """.formatted(userGroupTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(UserGroupSQLConstants.COL_USER_ID, user.getUserId());
        List<UUID> groupIds = jdbcTemplate.query(query, params, new SingleColumnRowMapper<>());
        query = """
                    DELETE FROM %s
                    WHERE USER_ID = :user_id
                    """.formatted(userGroupTable);
        jdbcTemplate.update(query, params);
        return groupIds;
    }

    @Override
    public List<UUID> deleteByGroup(Group group) {
        String query = """
                    SELECT USER_ID FROM %s
                    WHERE GROUP_ID = :group_id
                    """.formatted(userGroupTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_GROUP_ID, group.getGroupId());
        List<UUID> userIds = jdbcTemplate.query(query, params, new SingleColumnRowMapper<>());
        query = """
                    DELETE FROM %s
                    WHERE GROUP_ID = :group_id
                    """.formatted(userGroupTable);
        jdbcTemplate.update(query, params);
        return userIds;
    }
}
