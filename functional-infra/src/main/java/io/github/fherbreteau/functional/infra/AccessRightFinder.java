package io.github.fherbreteau.functional.infra;

import java.util.UUID;

import io.github.fherbreteau.functional.domain.entities.AccessRight;

public interface AccessRightFinder {

    AccessRight getAccess(UUID itemId, String attrOwner);
}
