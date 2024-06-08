package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.UserRepository;
import io.github.fherbreteau.functional.infra.mapper.BooleanResultExtractor;
import io.github.fherbreteau.functional.infra.mapper.UserExtractor;
import io.github.fherbreteau.functional.infra.mapper.UserGroupSQLConstant;
import io.github.fherbreteau.functional.infra.mapper.UserResultExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.UserGroupSQLConstant.COL_GROUP_ID;
import static io.github.fherbreteau.functional.infra.mapper.UserSQLConstant.*;
import static java.util.Optional.ofNullable;

@Transactional
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserResultExtractor userResultExtractor = new UserResultExtractor();
    private final UserExtractor userExtractor = new UserExtractor();
    private final BooleanResultExtractor existsExtractor = new BooleanResultExtractor();

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean exists(String name) {
        String query = """
                SELECT 1
                FROM "user"
                WHERE "name" = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public User findByName(String name) {
        String query = """
                SELECT u.id AS uid, u."name" AS uname, g.id AS gid, g."name" AS gname
                FROM "user" u
                LEFT JOIN user_group ON u.id = user_id
                LEFT JOIN "group" g ON g.id = group_id
                WHERE u."name" = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return jdbcTemplate.query(query, params, userResultExtractor);
    }

    @Override
    public boolean exists(UUID userId) {
        String query = """
                SELECT 1
                FROM "user"
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, userId);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public User findById(UUID userId) {
        String query = """
                SELECT u.id AS uid, u."name" AS uname, g.id AS gid, g."name" AS gname
                FROM "user" u
                LEFT JOIN user_group ON u.id = user_id
                LEFT JOIN "group" g ON g.id = group_id
                WHERE u.id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, userId);
        return jdbcTemplate.query(query, params, userResultExtractor);
    }

    @Override
    public User create(User user) {
        String query = """
                INSERT INTO "user"(id, name) VALUES (:id, :name)
                """;
        jdbcTemplate.update(query, userExtractor.map(user));
        query = """
                INSERT INTO user_group(user_id, group_id) VALUES (:user_id, :group_id)
                """;
        jdbcTemplate.batchUpdate(query, userExtractor.mapGroups(user));
        return user;
    }

    @Override
    public User update(User user) {
        User oldUser = ofNullable(findById(user.getUserId())).orElseGet(() -> findByName(user.getName()));
        String query = """
                DELETE FROM user_group WHERE user_id = :user_id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(UserGroupSQLConstant.COL_USER_ID, oldUser.getUserId());
        jdbcTemplate.update(query, params);
        if (!Objects.equals(user.getName(), oldUser.getName())) {
            query = """
                    UPDATE "user" SET name = :name WHERE id = :id
                    """;
            jdbcTemplate.update(query, userExtractor.map(user));
        } else if (!Objects.equals(user.getUserId(), oldUser.getUserId())) {
            query = """
                    UPDATE "user" SET id = :id WHERE name = :name
                    """;
            jdbcTemplate.update(query, userExtractor.map(user));
        }
        query = """
                INSERT INTO user_group(user_id, group_id) VALUES (:user_id, :group_id)
                """;
        jdbcTemplate.batchUpdate(query, userExtractor.mapGroups(user));
        return user;
    }

    @Override
    public void delete(User user) {
        String query = """
                DELETE FROM "user"
                WHERE id = :id
                AND "name" = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, user.getUserId())
                .addValue(COL_NAME, user.getName());
        jdbcTemplate.update(query, params);
    }

    @Override
    public User updatePassword(User user, String password) {
        String query = """
                UPDATE "user"
                SET password_hash = :password_hash
                WHERE id = :id
                AND "name" = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_ID, user.getUserId())
                .addValue(COL_NAME, user.getName())
                .addValue(COL_PASSWORD, password);
        jdbcTemplate.update(query, params);
        return user;
    }

    @Override
    public boolean checkPassword(User user, String password) {
        String query = """
                SELECT 1
                FROM "user"
                WHERE "name" = :name
                AND id = :id
                AND password_hash = :password_hash
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, user.getName())
                .addValue(COL_ID, user.getUserId())
                .addValue(COL_PASSWORD, password);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public boolean hasUserWithGroup(String name) {
        String query = """
                SELECT 1
                FROM user_group
                JOIN "group" g ON g.id = group_id
                WHERE g.name = :name
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, existsExtractor));
    }

    @Override
    public void removeGroupFromUser(Group group) {
        String query = """
                DELETE FROM user_group
                WHERE group_id = :group_id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(COL_GROUP_ID, group.getGroupId());
        jdbcTemplate.update(query, params);
    }
}
