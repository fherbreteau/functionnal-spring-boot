package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.domain.entities.AccessRight;

import java.util.UUID;

public interface AccessRightFinder {

    AccessRight getAccess(UUID itemId, String attrOwner);
}
