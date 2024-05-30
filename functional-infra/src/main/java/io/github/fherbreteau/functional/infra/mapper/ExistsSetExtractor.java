package io.github.fherbreteau.functional.infra.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExistsSetExtractor implements ResultSetExtractor<Boolean> {
    @Override
    public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
        return rs.next();
    }
}
