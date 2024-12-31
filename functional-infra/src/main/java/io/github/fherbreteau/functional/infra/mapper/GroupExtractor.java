package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.GroupSQLConstants.COL_ID;
import static io.github.fherbreteau.functional.infra.utils.GroupSQLConstants.COL_NAME;

import io.github.fherbreteau.functional.domain.entities.Group;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class GroupExtractor {
    public SqlParameterSource map(Group group) {
        return new MapSqlParameterSource()
                .addValue(COL_ID, group.getGroupId())
                .addValue(COL_NAME, group.getName());
    }
}
