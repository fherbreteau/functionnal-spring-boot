package io.github.fherbreteau.functional.infra;

import io.github.fherbreteau.functional.domain.entities.File;
import io.github.fherbreteau.functional.domain.entities.Folder;
import io.github.fherbreteau.functional.domain.entities.Item;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.driven.ContentRepository;
import io.github.fherbreteau.functional.driven.FileRepository;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.List;

@Repository
public class FileRepositoryImpl implements FileRepository, ContentRepository {

    private static final String NOT_IMPLEMENTED = "Not Implemented Yet";

    @Override
    public boolean exists(Folder parent, String name) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> I save(I item) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public List<Item> findByParentAndUser(Folder folder, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public <I extends Item> I findByNameAndParentAndUser(String name, Folder folder, User actor) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public InputStream readContent(File item) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    @Override
    public void writeContent(File item, InputStream content) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }
}
