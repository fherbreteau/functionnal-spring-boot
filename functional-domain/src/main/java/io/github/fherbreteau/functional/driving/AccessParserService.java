package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.ItemInput;

public interface AccessParserService {

    ItemInput parseAccessRights(String rights, Item item);
}
