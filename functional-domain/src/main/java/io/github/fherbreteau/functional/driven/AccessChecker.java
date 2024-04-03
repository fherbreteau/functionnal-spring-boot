package io.github.fherbreteau.functional.driven;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;

public interface AccessChecker {

    boolean canRead(Item<?, ?> item, User actor);

    boolean canWrite(Item<?, ?> item, User actor);

    boolean canExecute(Item<?, ?> item, User actor);

    boolean canChangeMode(Item<?, ?> item, User actor);

    boolean canChangeOwner(Item<?, ?> item, User actor);

    boolean canChangeGroup(Item<?, ?> item, User actor);
}
