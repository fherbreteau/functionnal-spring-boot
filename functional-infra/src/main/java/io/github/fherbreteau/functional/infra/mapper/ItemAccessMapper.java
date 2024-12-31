package io.github.fherbreteau.functional.infra.mapper;

import static io.github.fherbreteau.functional.infra.utils.ItemAccessSQLConstants.*;

import java.util.List;

import io.github.fherbreteau.functional.domain.entities.AccessRight;

public class ItemAccessMapper {
    public AccessRight map(List<String> accessTypes) {
        AccessRight right = AccessRight.none();
        if (accessTypes.contains(TYPE_READ)) {
            right = right.addRead();
        }
        if (accessTypes.contains(TYPE_WRITE)) {
            right = right.addWrite();
        }
        if (accessTypes.contains(TYPE_EXECUTE)) {
            right = right.addExecute();
        }
        return right;
    }
}
