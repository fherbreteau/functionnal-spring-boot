package io.github.fherbreteau.functional.driven.rules;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;

public interface AccessChecker {

    <T extends Item> boolean canRead(T item, User actor);

    <T extends Item> boolean canWrite(T item, User actor);

    <T extends Item> boolean canExecute(T item, User actor);

    <T extends Item> boolean canChangeMode(T item, User actor);

    <T extends Item> boolean canChangeOwner(T item, User actor);

    <T extends Item> boolean canChangeGroup(T item, User actor);
}
