package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;

import java.util.UUID;

public interface ItemFinder {

    UUID getItemId(Item item);

    Folder getFolder(UUID object);

    AccessRight getAccess(UUID itemId, String attrOwner);
}
