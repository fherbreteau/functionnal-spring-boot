package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.UserGroupSQLConstants.COL_GROUP_ID;
import static io.github.fherbreteau.functional.infra.utils.UserGroupSQLConstants.COL_USER_ID;

import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class UserGroupExtractor {

    public SqlParameterSource[] map(User user, List<UUID> groupIds) {
        return groupIds.stream()
                .map(groupId -> mapGroupLink(user.getUserId(), groupId))
                .toArray(SqlParameterSource[]::new);
    }

    public SqlParameterSource[] map(List<UUID> userIds, Group group) {
        return userIds.stream()
                .map(userId -> mapGroupLink(userId, group.getGroupId()))
                .toArray(SqlParameterSource[]::new);
    }

    private SqlParameterSource mapGroupLink(UUID userId, UUID groupId) {
        return new MapSqlParameterSource()
                .addValue(COL_USER_ID, userId)
                .addValue(COL_GROUP_ID, groupId);
    }
}
