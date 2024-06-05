package io.github.fherbreteau.functional.domain.command.impl.success;

import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.ItemRepository;

import java.io.InputStream;

public class UploadCommand extends AbstractSuccessItemCommand<Item> {
    private final ContentRepository contentRepository;
    private final File item;
    private final InputStream content;
    private final String contentType;

    public UploadCommand(ItemRepository repository, ContentRepository contentRepository, File item, InputStream content, String contentType) {
        super(repository);
        this.contentRepository = contentRepository;
        this.item = item;
        this.content = content;
        this.contentType = contentType;
    }

    @Override
    public Output<Item> execute(User actor) {
        File newItem = item.copyBuilder().withContentType(contentType).build();
        return contentRepository.writeContent(repository.save(newItem), content);
    }
}
