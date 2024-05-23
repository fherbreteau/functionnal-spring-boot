package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Item;

public interface AccessParserService {

    ItemInput parseAccessRights(String rights, Item item);
}
