package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.GroupSQLConstants.COL_ID;
import static io.github.fherbreteau.functional.infra.utils.GroupSQLConstants.COL_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.Group;
import org.springframework.jdbc.core.RowMapper;

public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Group.builder(rs.getString(COL_NAME))
                .withGroupId(rs.getObject(COL_ID, UUID.class)).build();
    }
}
