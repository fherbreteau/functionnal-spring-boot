package io.github.fherbreteau.functional.infra.mapper;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_GROUP_ID;
import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.COL_GROUP_NAME;
import static io.github.fherbreteau.functional.infra.mapper.UserSQLConstant.COL_USER_ID;
import static io.github.fherbreteau.functional.infra.mapper.UserSQLConstant.COL_USER_NAME;
import static java.util.Objects.nonNull;

public class UserResultExtractor implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }
        User.Builder builder = User.builder(rs.getString(COL_USER_NAME))
                .withUserId(rs.getObject(COL_USER_ID, UUID.class));
        Group group = processGroup(rs);
        if (nonNull(group)) {
            builder.withGroup(group);
        }
        return builder.addGroups(processRemainingGroup(rs))
                .build();
    }

    private List<Group> processRemainingGroup(ResultSet rs) throws SQLException {
        List<Group> groups = new ArrayList<>();
        while (rs.next()) {
            Group group = processGroup(rs);
            if (nonNull(group)) {
                groups.add(group);
            }
        }
        return groups;
    }

    private Group processGroup(ResultSet rs) throws SQLException {
        String name = rs.getString(COL_GROUP_NAME);
        if (rs.wasNull()) {
            return null;
        }
        UUID uuid = rs.getObject(COL_GROUP_ID, UUID.class);
        if (rs.wasNull()) {
            return null;
        }
        return Group.builder(name).withGroupId(uuid).build();
    }
}
