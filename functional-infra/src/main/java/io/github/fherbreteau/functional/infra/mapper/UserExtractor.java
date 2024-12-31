package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.UserSQLConstants.COL_ID;
import static io.github.fherbreteau.functional.infra.utils.UserSQLConstants.COL_NAME;

import io.github.fherbreteau.functional.domain.entities.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class UserExtractor {
    public SqlParameterSource map(User user) {
        return new MapSqlParameterSource()
                .addValue(COL_ID, user.getUserId())
                .addValue(COL_NAME, user.getName());
    }
}
