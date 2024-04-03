package io.github.fherbreteau.functional.driven;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;

public interface AccessChecker {

    <T extends Item<T, ?>> boolean canRead(T item, User actor);

    <T extends Item<T, ?>> boolean canWrite(T item, User actor);

    <T extends Item<T, ?>> boolean canExecute(T item, User actor);

    <T extends Item<T, ?>> boolean canChangeMode(T item, User actor);

    <T extends Item<T, ?>> boolean canChangeOwner(T item, User actor);

    <T extends Item<T, ?>> boolean canChangeGroup(T item, User actor);
}
