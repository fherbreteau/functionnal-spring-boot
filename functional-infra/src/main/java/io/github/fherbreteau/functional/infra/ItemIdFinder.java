package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.domain.entities.File;

import java.util.UUID;

public interface ItemIdFinder {

    UUID getItemId(File item);
}
