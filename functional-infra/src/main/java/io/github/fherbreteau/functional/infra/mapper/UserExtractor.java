package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.mapper.UserGroupSQLConstant.COL_GROUP_ID;
import static io.github.fherbreteau.functional.infra.mapper.UserGroupSQLConstant.COL_USER_ID;
import static io.github.fherbreteau.functional.infra.mapper.UserSQLConstant.COL_ID;
import static io.github.fherbreteau.functional.infra.mapper.UserSQLConstant.COL_NAME;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class UserExtractor {
    public SqlParameterSource map(User user) {
        return new MapSqlParameterSource()
                .addValue(COL_ID, user.getUserId())
                .addValue(COL_NAME, user.getName());
    }

    public SqlParameterSource[] mapGroups(User user) {
        return user.getGroups().stream()
                .map(group -> mapGroupLink(user, group))
                .toArray(SqlParameterSource[]::new);
    }

    private SqlParameterSource mapGroupLink(User user, Group group) {
        return new MapSqlParameterSource()
                .addValue(COL_USER_ID, user.getUserId())
                .addValue(COL_GROUP_ID, group.getGroupId());
    }
}
