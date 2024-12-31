package io.github.fherbreteau.functional.infra;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AccessRight;

public interface AccessRightRepository {

    AccessRight getAccess(UUID itemId, String attribution);

    void createAccess(UUID itemId, AccessRight accessRight, String attribution);

    void updateAccess(UUID itemId, AccessRight accessRight, String attribution);

    void deleteAccess(UUID itemId, String attribution);
}
