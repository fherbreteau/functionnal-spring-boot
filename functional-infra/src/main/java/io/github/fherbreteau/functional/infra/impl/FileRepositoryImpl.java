package io.github.fherbreteau.functional.infra.impl;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.FileRepository;
import io.github.fherbreteau.functional.infra.ItemIdFinder;
import io.github.fherbreteau.functional.infra.mapper.ExistsSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.COL_NAME;
import static io.github.fherbreteau.functional.infra.mapper.ItemSQLConstant.PARAM_PARENT_NAME;

@Repository
public class FileRepositoryImpl implements FileRepository, ItemIdFinder {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public FileRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean exists(Folder parent, String name) {
        String query = """
                SELECT 1
                FROM item i1
                JOIN item i2 ON i1.id = i2.parent_id
                WHERE i2.name = :parent_name
                AND i1.name = :item_name
                """;
        Map<String, Object> params = Map.of(
                PARAM_PARENT_NAME, parent.getName(),
                COL_NAME, name);
        return Boolean.TRUE.equals(jdbcTemplate.query(query, params, new ExistsSetExtractor()));
    }

    @Override
    public <I extends Item> I save(I item) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> void delete(I item) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public List<Item> findByParentAndUser(Folder folder, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> Optional<I> findByNameAndParentAndUser(String name, Folder folder, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public UUID getItemId(File item) {
        String query = """
                SELECT i1.id
                FROM item i1
                JOIN item i2 ON i1.id = i2.parent_id
                WHERE i2.name = :parent_name
                AND i1.name = :item_name
                """;
        Map<String, Object> params = Map.of(
                PARAM_PARENT_NAME, item.getParent().getName(),
                COL_NAME, item.getName());
        return jdbcTemplate.queryForObject(query, params, new SingleColumnRowMapper<>());
    }
}
