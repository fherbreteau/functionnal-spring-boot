package io.github.fherbreteau.functional.driven;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.path.Path;

import java.util.List;

public interface FileRepository {
    boolean exists(Folder parent, String name);

    <I extends Item<?, ?>> I save(I item);

    List<Item<?,?>> findByParentAndUser(Folder folder, User actor);

    <I extends Item<?,?>> I findByNameAndParentAndUser(String name, Folder folder, User actor);

    byte[] getContent(File item);

    Item<?,?> getItem(String root);
}
