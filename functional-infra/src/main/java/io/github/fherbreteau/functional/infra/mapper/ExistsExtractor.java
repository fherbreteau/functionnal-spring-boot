package io.github.fherbreteau.functional.infra.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ExistsExtractor implements ResultSetExtractor<Boolean> {
    @Override
    public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
        return rs.next();
    }
}
