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

public class UserResultExtractor implements ResultSetExtractor<User> {
    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }
        User.Builder builder = User.builder(rs.getString(COL_USER_NAME))
                .withUserId(rs.getObject(COL_USER_ID, UUID.class));
        builder.withGroup(processGroup(rs));
        return builder.addGroups(processRemainingGroup(rs))
                .build();
    }

    private List<Group> processRemainingGroup(ResultSet rs) throws SQLException {
        List<Group> groups = new ArrayList<>();
        while (rs.next()) {
            groups.add(processGroup(rs));
        }
        return groups;
    }

    private Group processGroup(ResultSet rs) throws SQLException {
        return Group.builder(rs.getString(COL_GROUP_NAME))
                .withGroupId(rs.getObject(COL_GROUP_ID, UUID.class))
                .build();
    }
}
