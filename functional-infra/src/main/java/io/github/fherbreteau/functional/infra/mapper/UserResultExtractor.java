package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.UserSQLConstants.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class UserResultExtractor implements ResultSetExtractor<User> {

    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }
        return User.builder(rs.getString(COL_UNAME))
                .withUserId(rs.getObject(COL_UID, UUID.class))
                .withGroups(processGroups(rs))
                .build();
    }

    private List<Group> processGroups(ResultSet rs) throws SQLException {
        List<Group> groups = new ArrayList<>();
        do {
            UUID id = rs.getObject(COL_GID, UUID.class);
            Optional.ofNullable(rs.getString(COL_GNAME))
                    .map(n -> buildGroup(n, id))
                    .ifPresent(groups::add);
        } while (rs.next());
        return groups;
    }

    private static Group buildGroup(String name, UUID id) {
        return Group.builder(name)
                .withGroupId(id)
                .build();
    }

}
