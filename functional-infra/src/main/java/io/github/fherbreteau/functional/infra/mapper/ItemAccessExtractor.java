package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class ItemAccessExtractor {

    public SqlParameterSource[] map(UUID itemId, AccessRight access, String attribution) {
        List<SqlParameterSource> sources = new ArrayList<>();
        map(sources, itemId, TYPE_READ, attribution, access.isRead());
        map(sources, itemId, TYPE_WRITE, attribution, access.isWrite());
        map(sources, itemId, TYPE_EXECUTE, attribution, access.isExecute());
        return sources.toArray(SqlParameterSource[]::new);
    }

    private void map(List<SqlParameterSource> sources, UUID itemId, String type, String attribution, boolean value) {
        sources.add(new MapSqlParameterSource()
                .addValue(COL_ITEM_ID, itemId)
                .addValue(COL_TYPE, type)
                .addValue(COL_ATTRIBUTION, attribution)
                .addValue(COL_VALUE, value));
    }
}
