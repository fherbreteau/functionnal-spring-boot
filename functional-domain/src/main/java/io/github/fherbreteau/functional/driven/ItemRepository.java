package io.github.fherbreteau.functional.driven;

import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    boolean exists(Folder parent, String name);

    <I extends Item> I save(I item);

    List<Item> findByParentAndUser(Folder folder, User actor);

    Optional<Item> findByNameAndParentAndUser(String name, Folder folder, User actor);

    <I extends Item> void delete(I item);
}
