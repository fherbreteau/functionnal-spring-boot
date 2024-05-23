package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.entities.*;

public interface FileService {

    Path getPath(String path, User currentUser);

    Output processCommand(ItemCommandType type, User currentUser, ItemInput itemInput);
}
