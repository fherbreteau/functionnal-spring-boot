package io.github.fherbreteau.functional.infra.impl;

import static io.github.fherbreteau.functional.infra.utils.ExtractorUtils.getGroupIds;
import static io.github.fherbreteau.functional.infra.utils.UserSQLConstants.*;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.infra.UserGroupRepository;
import io.github.fherbreteau.functional.infra.mapper.ExistsExtractor;
import io.github.fherbreteau.functional.infra.mapper.UserExtractor;
import io.github.fherbreteau.functional.infra.mapper.UserResultExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String groupTable;
    private final String userGroupTable;
    private final String userTable;
    private final UserGroupRepository userGroupRepository;
    private final UserResultExtractor userResultExtractor = new UserResultExtractor();
    private final UserExtractor userExtractor = new UserExtractor();
    private final ExistsExtractor existsExtractor = new ExistsExtractor();

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate,
                              @Value("${database.table.group:group}") String groupTable,
                              @Value("${database.table.user-group:user-group}") String userGroupTable,
                              @Value("${database.table.user:user}") String userTable,
                              UserGroupRepository userGroupRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupTable = groupTable;
        this.userGroupTable = userGroupTable;
        this.userTable = userTable;
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public boolean exists(String name) {
        String query = """
                SELECT 1 FROM %s
                WHERE NAME = :name
                """.formatted(userTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public User findByName(String name) {
        String query = """
                SELECT u.ID AS uid, u.NAME AS uname, g.ID AS gid, g.NAME AS gname
                FROM %s u
                LEFT JOIN %s ON u.ID = USER_ID
                LEFT JOIN %s g ON g.ID = GROUP_ID
                WHERE u.NAME = :name
                """.formatted(userTable, userGroupTable, groupTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return jdbcTemplate.query(query, params, userResultExtractor);
    }

    @Override
    public boolean exists(UUID userId) {
        String query = """
                SELECT 1 FROM %s
                WHERE ID = :id
                """.formatted(userTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, userId);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public User findById(UUID userId) {
        String query = """
                SELECT u.ID AS uid, u.NAME AS uname, g.ID AS gid, g.NAME AS gname
                FROM %s u
                LEFT JOIN %s ON u.ID = USER_ID
                LEFT JOIN %s g ON g.ID = GROUP_ID
                WHERE u.ID = :id
                """.formatted(userTable, userGroupTable, groupTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, userId);
        return jdbcTemplate.query(query, params, userResultExtractor);
    }

    @Override
    public User create(User user) {
        String query = """
                INSERT INTO %s (ID, NAME)
                VALUES (:id, :name)
                """.formatted(userTable);
        jdbcTemplate.update(query, userExtractor.map(user));
        userGroupRepository.create(user, getGroupIds(user));
        return user;
    }

    @Override
    public User update(User user) {
        User oldUser = ofNullable(findById(user.getUserId())).orElseGet(() -> findByName(user.getName()));
        if (!Objects.equals(user.getName(), oldUser.getName())) {
            String query = """
                    UPDATE %s SET NAME = :name
                    WHERE ID = :id
                    """.formatted(userTable);
            jdbcTemplate.update(query, userExtractor.map(user));
        } else if (!Objects.equals(user.getUserId(), oldUser.getUserId())) {
            List<UUID> groupIds = userGroupRepository.deleteByUser(oldUser);
            String query = """
                    UPDATE %s SET ID = :id
                    WHERE NAME = :name
                    """.formatted(userTable);
            jdbcTemplate.update(query, userExtractor.map(user));
            userGroupRepository.create(user, groupIds);
        }
        return user;
    }

    @Override
    public void delete(User user) {
        userGroupRepository.deleteByUser(user);
        String query = """
                DELETE FROM %s
                WHERE ID = :id AND NAME = :name
                """.formatted(userTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, user.getUserId())
                .addValue(COL_NAME, user.getName());
        jdbcTemplate.update(query, params);
    }

    @Override
    public User updatePassword(User user, String password) {
        String query = """
                UPDATE %s SET password_hash = :password_hash
                WHERE ID = :id AND NAME = :name
                """.formatted(userTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, user.getUserId())
                .addValue(COL_NAME, user.getName())
                .addValue(COL_PASSWORD, password);
        jdbcTemplate.update(query, params);
        return user;
    }

    @Override
    public String getPassword(User user) {
        String query = """
                SELECT password_hash FROM %s
                WHERE NAME = :name AND ID = :id
                """.formatted(userTable);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, user.getName())
                .addValue(COL_ID, user.getUserId());
        return jdbcTemplate.queryForObject(query, params, String.class);
    }

    @Override
    public boolean hasUserWithGroup(String name) {
        return userGroupRepository.existsByGroupName(name);
    }

    @Override
    public void removeGroupFromUser(Group group) {
        userGroupRepository.deleteByGroup(group);
    }
}
