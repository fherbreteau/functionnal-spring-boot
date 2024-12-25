package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.AccessRight;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.repository.ContentRepository;
import io.github.fherbreteau.functional.driven.repository.ItemRepository;
import io.github.fherbreteau.functional.driven.rules.AccessUpdater;

public class CreateFileCommand extends AbstractModifyItemCommand<Item> {

    private final String name;
    private final Folder parent;
    private final ContentRepository contentRepository;

    public CreateFileCommand(ItemRepository repository, ContentRepository contentRepository,
                             AccessUpdater accessUpdater, String name, Folder parent) {
        super(repository, accessUpdater);
        this.contentRepository = contentRepository;
        this.name = name;
        this.parent = parent;
    }

    @Override
    public Output<Item> execute(User actor) {
        File newFile = File.builder()
                .withName(name)
                .withParent(parent)
                .withOwner(actor)
                .withOwnerAccess(AccessRight.readWrite())
                .withGroupAccess(AccessRight.readOnly())
                .withOtherAccess(AccessRight.none())
                .build();
        return contentRepository.initContent(accessUpdater.createItem(repository.create(newFile)));
    }
}
