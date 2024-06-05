package io.github.fherbreteau.functional.infra.mapper;

import io.github.fherbreteau.functional.domain.entities.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.GroupSQLConstant.*;

public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Group.builder(rs.getString(COL_NAME))
                .withGroupId(rs.getObject(COL_ID, UUID.class)).build();
    }
}
