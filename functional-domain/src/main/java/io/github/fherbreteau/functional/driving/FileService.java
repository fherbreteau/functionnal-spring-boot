package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.entities.ItemCommandType;
import io.github.fherbreteau.functional.domain.entities.ItemInput;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.Path;
import io.github.fherbreteau.functional.domain.entities.User;

public interface FileService {

    Path getPath(String path, User currentUser);

    <T> Output<T> processCommand(ItemCommandType type, User currentUser, ItemInput itemInput);
}
